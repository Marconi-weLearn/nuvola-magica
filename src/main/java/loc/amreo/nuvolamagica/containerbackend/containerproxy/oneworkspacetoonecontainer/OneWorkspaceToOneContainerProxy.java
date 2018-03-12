package loc.amreo.nuvolamagica.containerbackend.containerproxy.oneworkspacetoonecontainer;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.CommunicationDriver;
import loc.amreo.nuvolamagica.containerbackend.ContainerManager;
import loc.amreo.nuvolamagica.containerbackend.ContainerProxy;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ProcessStatusResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.SignalProcessRequest;


@Service
@ConditionalOnExpression("'${nuvolamagica.container.proxy:OneWorkspaceToOneContainerProxy}'=='OneWorkspaceToOneContainerProxy'")
public class OneWorkspaceToOneContainerProxy implements ContainerProxy{
	
	@Autowired
	private ContainerInfoRepository containerInfoRepository;
	@Autowired
	private ContainerManager containerManager;
	@Autowired
	private CommunicationDriver communicationDriver;
	@Value("${nuvolamagica.container.proxy.container_name_prefix:nuvola-magica-bem-}")
	private String CONTAINER_NAME_PREFIX;
	
	@Override
	public void notifyWorkspaceCreation(UUID workspaceID) {
		ContainerInfo info = new ContainerInfo();
		info.setContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		info.setCommunicationEndpoint(containerManager.createContainer(info.getContainerName()));
		containerManager.pause(info.getContainerName());
		containerInfoRepository.save(info);
	}
	@Override
	public void notifySessionOpening(UUID workspaceID, UUID sessionID) {
		ContainerInfo info = containerInfoRepository.findOneByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		containerManager.unpause(info.getContainerName());
		info.addSession(sessionID);
		containerInfoRepository.save(info);
	}
	@Override
	public void notifySessionClosing(UUID workspaceID, UUID sessionID) {
		ContainerInfo info = containerInfoRepository.findOneByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		info.removeSession(sessionID);
		if (info.getSessions().isEmpty()) {
			containerManager.pause(info.getContainerName());	
		}
		containerInfoRepository.save(info);
	}
	@Override
	public void notifySessionClosing(UUID workspaceID) {
		ContainerInfo info = containerInfoRepository.findOneByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		containerManager.pause(info.getContainerName());
		info.setSessions(new HashSet<UUID>());
		containerInfoRepository.save(info);		
	}
	@Override
	public void notifyWorkspaceDeletion(UUID workspaceID) {
		containerManager.destroy(CONTAINER_NAME_PREFIX + workspaceID);
		containerInfoRepository.deleteAllByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
	}
	@Override
	public void uploadFile(UUID workspaceID, UUID sessionID, String filename, byte[] content) {
		//Get communication endpoint of the container
		ContainerInfo info = containerInfoRepository.findOneByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		communicationDriver.uploadFile(info.getCommunicationEndpoint(), workspaceID+"/"+filename, content);
	}
	@Override
	public void deleteFile(UUID workspaceID, UUID sessionID, String filename) {
		//Get communication endpoint of the container
		ContainerInfo info = containerInfoRepository.findOneByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		communicationDriver.deleteFile(info.getCommunicationEndpoint(), workspaceID+"/"+filename);
	}
	@Override
	public boolean existFile(UUID workspaceID, UUID sessionID, String filename) {
		//Get communication endpoint of the container
		ContainerInfo info = containerInfoRepository.findOneByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		return communicationDriver.existFile(info.getCommunicationEndpoint(), workspaceID+"/"+filename);
	}
	@Override
	public byte[] getFile(UUID workspaceID, UUID sessionID, String filename) {
		//Get communication endpoint of the container
		ContainerInfo info = containerInfoRepository.findOneByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		return communicationDriver.getFile(info.getCommunicationEndpoint(), workspaceID+"/"+filename);	
	}
	@Override
	public CompilationResponse build(UUID workspaceID, UUID sessionID, CompilationRequest compilationRequest) {
		//Get communication endpoint of the container
		ContainerInfo info = containerInfoRepository.findOneByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		compilationRequest.setChrootDir(workspaceID+"/"+compilationRequest.getChrootDir());
		return communicationDriver.build(info.getCommunicationEndpoint(), compilationRequest);	
	}
	@Override
	public UUID execute(UUID workspaceID, UUID sessionID, ExecutionRequest executionRequest) {
		//Get communication endpoint of the container
		ContainerInfo info = containerInfoRepository.findOneByContainerName(CONTAINER_NAME_PREFIX + workspaceID);
		executionRequest.setChrootDir(workspaceID+"/"+executionRequest.getChrootDir());
		return communicationDriver.startExecution(info.getCommunicationEndpoint(), executionRequest);		
	}
	@Override
	public Byte[] pullStdout(UUID processID) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Byte[] pullStderr(UUID processID) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void pushStdin(UUID processID, Byte[] content) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ProcessStatusResponse getProcessStatus(UUID processID) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void sendSignal(UUID processID, SignalProcessRequest signalInfo) {
		// TODO Auto-generated method stub
		
	}
}
