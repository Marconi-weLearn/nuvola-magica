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
	
	
	public UUID createWorkspace() throws Exception {
		Workspace ws = new Workspace();
		workspaceRepository.save(ws);
		containerProxy.notifyWorkspaceCreation(ws.getId());
		return ws.getId();
	}
	
	public Boolean isWorkspaceExisting(UUID workspaceID) throws Exception {
		return workspaceRepository.countWorkspaceByid(workspaceID) > 0;
	}

	public boolean deleteWorkspace(UUID workspaceID) throws Exception {
		if (isWorkspaceExisting(workspaceID)) {
			sessionService.deleteSessions(workspaceID);
			containerProxy.notifyWorkspaceDeletion(workspaceID);
			workspaceRepository.deleteWorkspaceByid(workspaceID);
			return true;
		} else {
			return false;
		}	
	}
}
