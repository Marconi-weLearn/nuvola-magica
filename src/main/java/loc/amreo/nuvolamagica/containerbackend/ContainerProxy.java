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

	public void uploadFile(UUID workspaceID, UUID sessionID, String filename, Byte[] content);
	public void deleteFile(UUID workspaceID, UUID sessionID, String filename);
	public byte[] getFile(UUID workspaceID, UUID sessionID, String filename);	
	
	public CompilationResponse compile(UUID workspaceID, UUID sessionID, CompilationRequest compilationRequest);
	public UUID execute(UUID workspaceID, UUID sessionID, ExecutionRequest executionRequest);
	public Byte[] pullStdout(UUID processID);
	public Byte[] pullStderr(UUID processID);
	public void pushStdin(UUID processID, Byte[] content);
	public ProcessStatusResponse getProcessStatus(UUID processID);
	public void sendSignal(UUID processID, SignalProcessRequest signalInfo);
}
