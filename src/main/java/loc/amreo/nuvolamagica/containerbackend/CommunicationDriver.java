package loc.amreo.nuvolamagica.containerbackend;

import java.util.UUID;

import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ProcessStatusResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.SignalProcessRequest;

public interface CommunicationDriver {

	void uploadFile(String communicationEndpoint, String filename, byte[] content) throws Exception;
	void deleteFile(String communicationEndpoint, String filename) throws Exception;
	boolean existFile(String communicationEndpoint, String filename) throws Exception;
	byte[] getFile(String communicationEndpoint, String filename) throws Exception;
	CompilationResponse build(String communicationEndpoint, CompilationRequest compilationRequest) throws Exception;
	UUID startExecution(String communicationEndpoint, ExecutionRequest compilationRequest) throws Exception;
	byte[] pullProcessStdout(String communicationEndpoint, UUID processID) throws Exception;
	byte[] pullProcessStderr(String communicationEndpoint, UUID processID) throws Exception;
	void pushProcessStdin(String communicationEndpoint, UUID processID, byte[] content) throws Exception;
	ProcessStatusResponse getProcessStatus(String communicationEndpoint, UUID processID) throws Exception;
	void sendSignalToProcess(String communicationEndpoint, UUID processID, SignalProcessRequest signalInfo) throws Exception;

}
