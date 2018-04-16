package loc.amreo.nuvolamagica.containerbackend;

import java.util.UUID;

import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ProcessStatusResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.SignalProcessRequest;

public interface ContainerProxy {
	public void notifyWorkspaceCreation(UUID workspaceID);
	public void notifySessionOpening(UUID workspaceID, UUID sessionID);
	public void notifySessionClosing(UUID workspaceID, UUID sessionID);
	public void notifySessionClosing(UUID workspaceID);
	public void notifyWorkspaceDeletion(UUID workspaceID);

	public void uploadFile(UUID workspaceID, UUID sessionID, String filename, byte[] content);
	public void deleteFile(UUID workspaceID, UUID sessionID, String filename);
	public boolean existFile(UUID workspaceID, UUID sessionID, String filename);
	public byte[] getFile(UUID workspaceID, UUID sessionID, String filename);	
	
	public CompilationResponse build(UUID workspaceID, UUID sessionID, CompilationRequest compilationRequest);
	public UUID execute(UUID workspaceID, UUID sessionID, ExecutionRequest executionRequest);
	public byte[] pullStdout(UUID workspaceID, UUID sessionID,UUID processID);
	public byte[] pullStderr(UUID workspaceID, UUID sessionID,UUID processID);
	public void pushStdin(UUID workspaceID, UUID sessionID,UUID processID, byte[] content);
	public ProcessStatusResponse getProcessStatus(UUID workspaceID, UUID sessionID,UUID processID);
	public void sendSignal(UUID workspaceID, UUID sessionID, UUID processID, SignalProcessRequest signalInfo);

}
