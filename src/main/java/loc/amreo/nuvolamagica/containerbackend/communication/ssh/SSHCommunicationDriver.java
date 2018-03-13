package loc.amreo.nuvolamagica.containerbackend.communication.ssh;

import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.CommunicationDriver;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ProcessStatusResponse;

@Service
@ConditionalOnExpression("'${nuvolamagica.communication.driver:SSH}'=='SSH'")
public class SSHCommunicationDriver implements CommunicationDriver {

	@Override
	public void uploadFile(String communicationEndpoint, String filename, byte[] content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFile(String communicationEndpoint, String filename) {
		// TODO Auto-generated method sterroring ub
		
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

}
