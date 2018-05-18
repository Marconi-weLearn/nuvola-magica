package loc.amreo.nuvolamagica.containerbackend.communication.ssh;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

	private SSHClient getConnection(String endpoint) {
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
	public void uploadFile(String communicationEndpoint, String filename, byte[] content) {
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
	public void deleteFile(String communicationEndpoint, String filename) {
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
	public boolean existFile(String communicationEndpoint, String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] getFile(String communicationEndpoint, String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompilationResponse build(String communicationEndpoint, CompilationRequest compilationRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID startExecution(String communicationEndpoint, ExecutionRequest compilationRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] pullProcessStdout(String communicationEndpoint, UUID processID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] pullProcessStderr(String communicationEndpoint, UUID processID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pushProcessStdin(String communicationEndpoint, UUID processID, byte[] content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProcessStatusResponse getProcessStatus(String communicationEndpoint, UUID processID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendSignalToProcess(String communicationEndpoint, UUID processID, SignalProcessRequest signalInfo) {
		// TODO Auto-generated method stub
		
	}

}
