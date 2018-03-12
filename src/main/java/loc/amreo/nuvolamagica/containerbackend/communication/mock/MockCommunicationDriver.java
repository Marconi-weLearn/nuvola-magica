package loc.amreo.nuvolamagica.containerbackend.communication.mock;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.CommunicationDriver;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionRequest;

@Service
@ConditionalOnExpression("'${nuvolamagica.communication.driver}'=='mock'")
public class MockCommunicationDriver implements CommunicationDriver {

	@Override
	public void uploadFile(String communicationEndpoint, String filename, byte[] content) {
		System.out.println("[MockCommunicationDriver][uploadFile] I think I've uploaded the file " + filename + " with content: " + new String(content, StandardCharsets.UTF_8) + " to " + communicationEndpoint);
	}

	@Override
	public void deleteFile(String communicationEndpoint, String filename) {
		System.out.println("[MockCommunicationDriver][deleteFile] I think I've deleted the file " + filename + " to " + communicationEndpoint);
	}

	@Override
	public boolean existFile(String communicationEndpoint, String filename) {
		System.out.println("[MockCommunicationDriver][existFile] I think I've checked the file " + filename + " to " + communicationEndpoint + " and I think it exist");
		return true; //I don't know why but the file always exist.
	}

	@Override
	public byte[] getFile(String communicationEndpoint, String filename) {
		System.out.println("[MockCommunicationDriver][getFile] I think I've downloaded the file " + filename + " from " + communicationEndpoint);	
		return "The secret content".getBytes(); //I don't know why the content of the file is always this. Maybe I return a constant?
	}

	@Override
	public CompilationResponse build(String communicationEndpoint, CompilationRequest compilationRequest) {
		System.out.println("[MockCommunicationDriver][build] I think I've built the project " + 
				compilationRequest.getLangType() + " in " + compilationRequest.getChrootDir() + " @" + communicationEndpoint);	
		return new CompilationResponse(0, " The project is built sucessfull but I don't know why.");
	}

	@Override
	public UUID startExecution(String communicationEndpoint, ExecutionRequest compilationRequest) {
		System.out.println("[MockCommunicationDriver][startExecution] I think I've started the process " + 
				compilationRequest.getLangType() + " in " + compilationRequest.getChrootDir() + " @" + communicationEndpoint);	
		return new UUID(666,666);
	}
}
