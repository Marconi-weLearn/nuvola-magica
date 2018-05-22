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

import javax.ws.rs.NotFoundException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
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
import static loc.amreo.nuvolamagica.Utils.null2empty;
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
	private final String USERNAME;
	private final String PASSWORD;
	private final String DIRECTORY_OF_FILES;
	private final HashMap<String, SSHClient> connections;
	private final HashMap<UUID, Command> runningCommands;
	private final Logger logger;
	
	public SSHCommunicationDriver(@Autowired SSHLangDriverRegistry langDriverRegistry, 
			@Value("${nuvolamagica.communication.username:root}") String USERNAME,
			@Value("${nuvolamagica.communication.password:password}") String PASSWORD,
			@Value("${nuvolamagica.communication.ssh.directory_of_files:/var/nuvolamagica/}") String DIRECTORY_OF_FILES) {
		super();
		this.langDriverRegistry = langDriverRegistry;
		this.USERNAME = USERNAME;
		this.PASSWORD = PASSWORD;
		this.connections = new HashMap<>();
		this.DIRECTORY_OF_FILES = DIRECTORY_OF_FILES;
		this.logger = Logger.getLogger(SSHCommunicationDriver.class);
		this.runningCommands = new HashMap<>();
	}

	private SSHClient getConnection(String endpoint) throws Exception {
		//Log
		logger.info("Getting connection endpoint=" + endpoint);
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
			newconn.addHostKeyVerifier(new PromiscuousVerifier());
			newconn.connect(hostname, port);
			newconn.authPassword(USERNAME, PASSWORD);
			connections.put(endpoint, newconn);
			//Return the connection
			return newconn;
		}
	}
		
	private Command simpleExecCommand(Session session, String command, int timeout) throws Exception {
		//Log the command
		logger.debug(command);
		//Execute the command
		Command cmd = session.exec(command);
		//Wait the end of the cmd
		//FIXME: is join blocking?
		cmd.join(timeout, TimeUnit.SECONDS);
		//Return it
		return cmd;
	}
	
	@Override
	public void uploadFile(String communicationEndpoint, String filename, byte[] content) throws Exception {
		logger.info("Uploading file filename=" + DIRECTORY_OF_FILES+filename + " endpoint=" + communicationEndpoint + 
				" content.length=" + content.length);
		SSHClient conn = getConnection(communicationEndpoint);
		//Create empty file with directory
		simpleExecCommand(conn.startSession(), "mkfile " + DIRECTORY_OF_FILES+filename, 1);
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
		
		sftp.put(srcFile, DIRECTORY_OF_FILES+filename);	
		logger.info("File uploaded");
	}

	@Override
	public void deleteFile(String communicationEndpoint, String filename) throws Exception {
		logger.info("Deleting file filename=" + DIRECTORY_OF_FILES+filename + " endpoint=" + communicationEndpoint);
		//Remove the file if exist
		simpleExecCommand(getConnection(communicationEndpoint).startSession(), "rm -rf " + DIRECTORY_OF_FILES+filename, 1);
		logger.info("File deleted");
	}

	@Override
	public boolean existFile(String communicationEndpoint, String filename) throws Exception {
		logger.info("Checking the existence of file filename=" + DIRECTORY_OF_FILES+filename + " endpoint=" + communicationEndpoint);
		SSHClient conn = getConnection(communicationEndpoint);
		//Check if the file exist
		return simpleExecCommand(conn.startSession(), "test -f " + DIRECTORY_OF_FILES+filename, 1).getExitStatus() == 0;
	}

	@Override
	public byte[] getFile(String communicationEndpoint, String filename) throws Exception {
		logger.info("Getting the file filename=" + DIRECTORY_OF_FILES+filename + " endpoint=" + communicationEndpoint);
		//It assume the existence of the file
		SSHClient conn = getConnection(communicationEndpoint);
	
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
		sftp.get(DIRECTORY_OF_FILES+filename, remote);
		logger.info("File getted size=" + out.size());
		//Extract the bytes from the byte array
		return out.toByteArray();
	}

	@Override
	public CompilationResponse build(String communicationEndpoint, CompilationRequest compilationRequest) throws Exception {
		logger.info("Building chroot=" + DIRECTORY_OF_FILES+compilationRequest.getChrootDir() + 
				" lang=" + compilationRequest.getLangType() + " mainFile=" + compilationRequest.getMainFile() + 
				" options=" + compilationRequest.getOptions() + " endpoint=" + communicationEndpoint);
		compilationRequest.setChrootDir(DIRECTORY_OF_FILES+compilationRequest.getChrootDir());
		
		//Get the command text
		Optional<String> cmdText = langDriverRegistry.getBuildCommand(compilationRequest.getLangType(), 
				compilationRequest.getMainFile(), compilationRequest.getOptions());
		//Check if the language exist
		if (!cmdText.isPresent())
			return new CompilationResponse(-1, "Language doesn't exist or configured");
		
		//Execute the command
		Command cmd = simpleExecCommand(getConnection(communicationEndpoint).startSession() , "securerun " + compilationRequest.getChrootDir() + " " + cmdText.get(), 1);
		
		//Get the exist status
		String outMsg = null2empty(IOUtils.toString(cmd.getInputStream())) + " " + null2empty(IOUtils.toString(cmd.getErrorStream())) + " " + null2empty(cmd.getExitErrorMessage());
		logger.debug("build status=" + cmd.getExitStatus());

		//When the file exist, test return 0; When the file doesn't exist, test return 1
		return new CompilationResponse(cmd.getExitStatus(), outMsg);
	}

	@Override
	public UUID startExecution(String communicationEndpoint, ExecutionRequest compilationRequest) throws Exception {
		UUID id = UUID.randomUUID();
		logger.info("Executing chroot=" + DIRECTORY_OF_FILES+compilationRequest.getChrootDir() + 
				" lang=" + compilationRequest.getLangType() + " mainFile=" + compilationRequest.getMainFile() + 
				" options=" + compilationRequest.getOptions() + " endpoint=" + communicationEndpoint);
		compilationRequest.setChrootDir(DIRECTORY_OF_FILES+compilationRequest.getChrootDir());

		//Get the command text
		Optional<String> cmdText = langDriverRegistry.getExecuteCommand(compilationRequest.getLangType(), 
				compilationRequest.getMainFile(), compilationRequest.getOptions());
		//Check if the language exist
		if (!cmdText.isPresent()) {
			throw new Exception("Language doesn't exist or configured");
		}
		
		//Execute the command
		Command cmd = simpleExecCommand(getConnection(communicationEndpoint).startSession() , "securerun " + compilationRequest.getChrootDir() + " " + cmdText.get(), 0);
		//Save the command in runningCommands
		runningCommands.put(id, cmd);
		
		return id;
	}

	@Override
	public byte[] pullProcessStdout(String communicationEndpoint, UUID processID) throws Exception {
		logger.info("Pulling stdout processID=" + processID + " endpoint=" + communicationEndpoint);
		//Check if the command is running
		if (!runningCommands.containsKey(processID)) {
			throw new NotFoundException("Command not found");
		}
		
		//Get the command
		Command cmd = runningCommands.get(processID);
		
		//FIXME: check if the inputStream == stdout of the process
		return IOUtils.toByteArray(cmd.getInputStream());
	}

	@Override
	public byte[] pullProcessStderr(String communicationEndpoint, UUID processID) throws Exception {
		logger.info("Pulling stderr processID=" + processID + " endpoint=" + communicationEndpoint);
		//Check if the command is running
		if (!runningCommands.containsKey(processID)) {
			throw new NotFoundException("Command not found");
		}
		
		//Get the command
		Command cmd = runningCommands.get(processID);
		
		//FIXME: check if the inputStream == stdout of the process
		return IOUtils.toByteArray(cmd.getErrorStream());
	}

	@Override
	public void pushProcessStdin(String communicationEndpoint, UUID processID, byte[] content) throws Exception {
		logger.info("Pushing stdin processID=" + processID + " content.length=" + content.length + " endpoint=" + communicationEndpoint);
		//Check if the command is running
		if (!runningCommands.containsKey(processID)) {
			throw new NotFoundException("Command not found");
		}
		
		//Get the command
		Command cmd = runningCommands.get(processID);
		
		//Write the content to the stdin?
		cmd.getOutputStream().write(content);
	}

	@Override
	public ProcessStatusResponse getProcessStatus(String communicationEndpoint, UUID processID) throws Exception {
		logger.info("Getting process status processID=" + processID + " endpoint=" + communicationEndpoint);
		//Check if the command is running
		if (!runningCommands.containsKey(processID)) {
			throw new NotFoundException("Command not found");
		}
		
		//Get the command
		Command cmd = runningCommands.get(processID);
		
		return new ProcessStatusResponse(cmd.getExitStatus(), cmd.getExitErrorMessage());
	
	}

	@Override
	public void sendSignalToProcess(String communicationEndpoint, UUID processID, SignalProcessRequest signalInfo) throws Exception {
		logger.info("Sending signal to process processID=" + processID + " signal=" + signalInfo.getSignal() + " endpoint=" + communicationEndpoint);
		//Check if the command is running
		if (!runningCommands.containsKey(processID)) {
			throw new NotFoundException("Command not found");
		}
		
		//Get the command
		Command cmd = runningCommands.get(processID);
	
		cmd.signal(signalInfo.getSignal());
	}

}
