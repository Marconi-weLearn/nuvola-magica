package loc.amreo.nuvolamagica.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID>{
	
	public int countWorkspaceByid(UUID ID);
}
