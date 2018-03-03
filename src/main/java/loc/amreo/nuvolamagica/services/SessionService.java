package loc.amreo.nuvolamagica.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.ContainerProxy;
import loc.amreo.nuvolamagica.repositories.Session;
import loc.amreo.nuvolamagica.repositories.SessionRepository;

@Service
public class SessionService {

	@Autowired
	private WorkspaceService workspaceService;
	@Autowired
	private SessionRepository sessionRepository;
	@Autowired
	private ContainerProxy containerProxy;
	
	@Value("${nuvolamagica.session.max_session_time:28800}")
	private int MAX_SESSION_TIME; 
	
	@Value("${nuvolamagica.session.max_inactivity_time:600}")
	private int MAX_INACTIVITY_TIME; 
	
	private LocalDateTime min(LocalDateTime a, LocalDateTime b) {
		return a.getNano() < b.getNano() ? a : b;
	}
	
	private LocalDateTime calculateTimeoutDate(LocalDateTime creationDate) {
		return min(creationDate.plusSeconds(MAX_SESSION_TIME), creationDate.plusSeconds(MAX_INACTIVITY_TIME));
	}
	
	public Optional<UUID> createSession(UUID workspaceID) {
		if (workspaceService.isWorkspaceExisting(workspaceID)) {
			Session ss = new Session();
			ss.setWorkspaceID(workspaceID);
			ss.setTimeoutDate(calculateTimeoutDate(LocalDateTime.now()));
			sessionRepository.save(ss);
			containerProxy.notifySessionOpening(workspaceID, ss.getId());
			return Optional.of(ss.getId());
		} else {
			return Optional.empty();
		}
	}
	
	public boolean isSessionExisting(UUID workspaceID, UUID sessionID) {
		return sessionRepository.countSessionByidAndWorkspaceID(sessionID, workspaceID) > 0;
	}
	
	public boolean wakeUpSession(UUID workspaceID, UUID sessionID) {
		if (isSessionExisting(workspaceID, sessionID)) {
			Session ss = sessionRepository.findSessionByidAndWorkspaceID(sessionID, workspaceID);
			ss.setTimeoutDate(calculateTimeoutDate(ss.getCreationDate()));
			return true;
		} else {
			return false;
		}
	}
	
	public boolean deleteSession(UUID workspaceID, UUID sessionID) {
		if (isSessionExisting(workspaceID, sessionID)) {
			containerProxy.notifySessionClosing(workspaceID, sessionID);
			sessionRepository.deleteSessionByidAndWorkspaceID(sessionID, workspaceID);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean deleteSessions(UUID workspaceID) {
		if (workspaceService.isWorkspaceExisting(workspaceID)) {
			containerProxy.notifySessionClosing(workspaceID);
			sessionRepository.deleteAllSessionByWorkspaceID(workspaceID);
			return true;
		} else {
			return false;
		}
	}
}
