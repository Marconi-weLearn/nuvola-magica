package loc.amreo.nuvolamagica.containerbackend;

import java.util.UUID;

import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ProcessStatusResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.SignalProcessRequest;

public interface ContainerProxy {
	public void notifyWorkspaceCreation(UUID workspaceID) throws Exception;
	public void notifySessionOpening(UUID workspaceID, UUID sessionID) throws Exception;
	public void notifySessionClosing(UUID workspaceID, UUID sessionID) throws Exception;
	public void notifySessionClosing(UUID workspaceID) throws Exception;
	public void notifyWorkspaceDeletion(UUID workspaceID) throws Exception;

	public void uploadFile(UUID workspaceID, UUID sessionID, String filename, byte[] content) throws Exception;
	public void deleteFile(UUID workspaceID, UUID sessionID, String filename) throws Exception;
	public boolean existFile(UUID workspaceID, UUID sessionID, String filename) throws Exception;
	public byte[] getFile(UUID workspaceID, UUID sessionID, String filename) throws Exception;	
	
	public CompilationResponse build(UUID workspaceID, UUID sessionID, CompilationRequest compilationRequest) throws Exception;
	public UUID execute(UUID workspaceID, UUID sessionID, ExecutionRequest executionRequest) throws Exception;
	public byte[] pullStdout(UUID workspaceID, UUID sessionID,UUID processID) throws Exception;
	public byte[] pullStderr(UUID workspaceID, UUID sessionID,UUID processID) throws Exception;
	public void pushStdin(UUID workspaceID, UUID sessionID,UUID processID, byte[] content) throws Exception;
	public ProcessStatusResponse getProcessStatus(UUID workspaceID, UUID sessionID,UUID processID) throws Exception;
	public void sendSignal(UUID workspaceID, UUID sessionID, UUID processID, SignalProcessRequest signalInfo) throws Exception;

}
