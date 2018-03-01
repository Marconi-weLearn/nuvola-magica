package loc.amreo.nuvolamagica.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.repositories.SessionRepository;
import loc.amreo.nuvolamagica.repositories.Workspace;
import loc.amreo.nuvolamagica.repositories.WorkspaceRepository;


@Service
public class WorkspaceService {
	
	//@Autowired
	//private ContainerRegistry containerRegistry;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private WorkspaceRepository workspaceRepository;
	
	
	public UUID createWorkspace() {
		Workspace ws = new Workspace();
		workspaceRepository.save(ws);
		//TODO: the registry should be notified of the new UUID
		return ws.getId();
	}
	
	public Boolean isWorkspaceExisting(UUID workspaceID) {
		return workspaceRepository.countWorkspaceByid(workspaceID) > 0;
	}

	public boolean deleteWorkspace(UUID workspaceID) {
		if (isWorkspaceExisting(workspaceID)) {
			//NOTIFY the container registry
			sessionService.deleteSessions(workspaceID);
			workspaceRepository.deleteWorkspaceByid(workspaceID);
			return true;
		} else {
			return false;
		}	
	}
}
