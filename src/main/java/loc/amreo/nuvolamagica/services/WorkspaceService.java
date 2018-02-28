package loc.amreo.nuvolamagica.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.repositories.Workspace;
import loc.amreo.nuvolamagica.repositories.WorkspaceRepository;


@Service
public class WorkspaceService {
	
	//@Autowired
	//private ContainerRegistry containerRegistry;
	
	@Autowired
	private WorkspaceRepository workspaceRepository;
	
	public UUID createWorkspace() {
		UUID uuid = UUID.randomUUID();
		//TODO: the repository should be queryied and updated
		workspaceRepository.save(new Workspace(uuid));
		//TODO: the registry should be notified of the new UUID
		return uuid;
	}
}
