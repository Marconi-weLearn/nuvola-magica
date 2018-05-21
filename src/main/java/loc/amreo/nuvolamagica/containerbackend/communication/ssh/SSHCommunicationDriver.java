package loc.amreo.nuvolamagica.containerbackend.communication.ssh;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;


import loc.amreo.nuvolamagica.containerbackend.CommunicationDriver;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ProcessStatusResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.SignalProcessRequest;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.InMemorySourceFile;
import net.schmizz.sshj.xfer.LocalDestFile;

@Service
@ConditionalOnExpression("'${nuvolamagica.communication.driver:ssh}'=='ssh'")
public class SSHCommunicationDriver implements CommunicationDriver {

	private final SSHLangDriverRegistry langDriverRegistry;
	private final String username;
	private final String password;
	private final String directoryOfFiles;
	private final HashMap<String, SSHClient> connections;
	
	
	public SSHCommunicationDriver(@Autowired SSHLangDriverRegistry langDriverRegistry, 
			@Value("${nuvolamagica.communication.username:root}") String username,
			@Value("${nuvolamagica.communication.password:password}") String password,
			@Value("${nuvolamagica.communication.ssh.directory_of_files:/var/nuvolamagica/}") String directoryOfFiles) {
		super();
		this.langDriverRegistry = langDriverRegistry;
		this.username = username;
		this.password = password;
		this.connections = new HashMap<>();
		this.directoryOfFiles = directoryOfFiles;
	}

	private SSHClient getConnection(String endpoint) throws Exception {
		//Check already exist a connection to the endpoint
		if (connections.containsKey(endpoint)) {
			//Return the connection
			return connections.get(endpoint);
		} else {
			//Create the connection
			SSHClient newconn = new SSHClient();
			//Get the hostname and port
			String hostname = endpoint.substring(0, endpoint.indexOf(":"));
			int port = Integer.parseInt(endpoint.substring(endpoint.indexOf(":")+1));
			//Connect to the container
			try {
				newconn.addHostKeyVerifier(new PromiscuousVerifier());
				newconn.connect(hostname, port);
				newconn.authPassword(username, password);
			} catch (IOException e) {
				e.printStackTrace();
			}
			connections.put(endpoint, newconn);
			
			//Return the connection
			return newconn;
		}
	}
	
	@Override
	public void uploadFile(String communicationEndpoint, String filename, byte[] content) throws Exception {
		SSHClient conn = getConnection(communicationEndpoint);
		try {
			//Create empty file with dire
			Session session = conn.startSession();
			Command cmd = session.exec("mkfile " + directoryOfFiles+filename);
			cmd.join(1, TimeUnit.SECONDS);
			
			session.close();
			//Upload the content of the file
			SFTPClient sftp = conn.newSFTPClient();
			InMemorySourceFile srcFile = new InMemorySourceFile() {
				
				@Override
				public String getName() {
					return filename;
				}
				
				@Override
				public long getLength() {
					return content.length;
				}
				
				@Override
				public InputStream getInputStream() throws IOException {
					return new ByteArrayInputStream(content);
				}
			};
			
			sftp.put(srcFile, directoryOfFiles+filename);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void deleteFile(String communicationEndpoint, String filename) throws Exception {
		SSHClient conn = getConnection(communicationEndpoint);
		try {
			//Remove the file if exist
			Session session = conn.startSession();
			Command cmd = session.exec("rm -rf " + directoryOfFiles+filename);
			cmd.join(1, TimeUnit.SECONDS);
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

	@Override
	public boolean existFile(String communicationEndpoint, String filename) throws Exception {
		SSHClient conn = getConnection(communicationEndpoint);
		try {
			//Check if the file
			Session session = conn.startSession();
			Command cmd = session.exec("test -f " + directoryOfFiles+filename);
			cmd.join(1, TimeUnit.SECONDS);
			session.close();
			//When the file exist, test return 0; When the file doesn't exist, test return 1
			return cmd.getExitStatus() == 0;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}			
	}

	@Override
	public byte[] getFile(String communicationEndpoint, String filename) throws Exception {
		//It assume the existence of the file
		SSHClient conn = getConnection(communicationEndpoint);
		try {
			//Create the SFTP client
			SFTPClient sftp = conn.newSFTPClient();
			//Create the output stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			LocalDestFile remote = new LocalDestFile() {
				
				@Override
				public void setPermissions(int arg0) throws IOException {
					//Nothing
				}
				
				@Override
				public void setLastModifiedTime(long arg0) throws IOException {
					//Nothing
				}
				
				@Override
				public void setLastAccessedTime(long arg0) throws IOException {
					//Nothing
				}
				
				@Override
				public LocalDestFile getTargetFile(String arg0) throws IOException {
					//I don't know why but i can't return null. Why is there this method?
					return this;
				}
				
				@Override
				public LocalDestFile getTargetDirectory(String arg0) throws IOException {
					//I don't know why but i can't return null. Why is there this method?
					return this;
				}
				
				@Override
				public OutputStream getOutputStream() throws IOException {
					return out;
				}
				
				@Override
				public LocalDestFile getChild(String arg0) {
					return null;
				}
			};
			//Download the file
			sftp.get(directoryOfFiles+filename, remote);
			//Extract the bytes from the byte array
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}	
	}

	@Override
	public CompilationResponse build(String communicationEndpoint, CompilationRequest compilationRequest) throws Exception {
		SSHClient conn = getConnection(communicationEndpoint);
		try {
			//Exec the command for building the project
			Session session = conn.startSession();
			//TODO: Set chroot
			//Get the command text
			Optional<String> cmdText = langDriverRegistry.getBuildCommand(compilationRequest.getLangType(), 
					directoryOfFiles+compilationRequest.getMainFile(), compilationRequest.getOptions());
			
			//Check if the language exist
			if (!cmdText.isPresent())
				return new CompilationResponse(-1, "Language doesn't exist or configured");
			
			//Execute the command
			Command cmd = session.exec(cmdText.get());
			//outMsg should contain [stdout] + [stderr] + [error message]
			cmd.join(1, TimeUnit.SECONDS);
			String outMsg = IOUtils.toString(cmd.getInputStream()) + IOUtils.toString(cmd.getErrorStream()) + cmd.getExitErrorMessage();
			session.close();
			
			//When the file exist, test return 0; When the file doesn't exist, test return 1
			return new CompilationResponse(cmd.getExitStatus(), outMsg);
		} catch (IOException e) {
			e.printStackTrace();
			return new CompilationResponse(-1, "Build failed because " + e.getMessage());
		}	
	}

	@Override
	public UUID startExecution(String communicationEndpoint, ExecutionRequest compilationRequest) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] pullProcessStdout(String communicationEndpoint, UUID processID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] pullProcessStderr(String communicationEndpoint, UUID processID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pushProcessStdin(String communicationEndpoint, UUID processID, byte[] content) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProcessStatusResponse getProcessStatus(String communicationEndpoint, UUID processID) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendSignalToProcess(String communicationEndpoint, UUID processID, SignalProcessRequest signalInfo) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
