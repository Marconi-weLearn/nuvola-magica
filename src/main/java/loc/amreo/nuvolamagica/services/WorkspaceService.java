package loc.amreo.nuvolamagica.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.ContainerProxy;
import loc.amreo.nuvolamagica.repositories.Workspace;
import loc.amreo.nuvolamagica.repositories.WorkspaceRepository;


@Service
public class WorkspaceService {
	
	@Autowired
	private ContainerProxy containerProxy;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private WorkspaceRepository workspaceRepository;
	
	
	public UUID createWorkspace() {
		Workspace ws = new Workspace();
		workspaceRepository.save(ws);
		containerProxy.notifyWorkspaceCreation(ws.getId());
		return ws.getId();
	}
	
	public Boolean isWorkspaceExisting(UUID workspaceID) {
		return workspaceRepository.countWorkspaceByid(workspaceID) > 0;
	}

	public boolean deleteWorkspace(UUID workspaceID) {
		if (isWorkspaceExisting(workspaceID)) {
			containerProxy.notifyWorkspaceDeletion(workspaceID);
			sessionService.deleteSessions(workspaceID);
			workspaceRepository.deleteWorkspaceByid(workspaceID);
			return true;
		} else {
			return false;
		}	
	}
}
