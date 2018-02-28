package loc.amreo.nuvolamagica.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID>{

	public int countSessionByidAndWorkspaceID(UUID ID, UUID workspaceID);
	
	public Session findSessionByidAndWorkspaceID(UUID ID, UUID workspaceID);
}
