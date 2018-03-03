package loc.amreo.nuvolamagica.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.ContainerProxy;

@Service
public class FilesystemService {
	
	@Autowired
	private SessionService sessionService;
	@Autowired
	private ContainerProxy containerProxy;
	
	public boolean put(UUID workspaceID, UUID sessionID, String filename, byte[] content) {
		if (sessionService.isSessionExisting(workspaceID, sessionID)) {
			containerProxy.uploadFile(workspaceID, sessionID, filename, content);
			return true;
		} else {
			return false;
		}
	}

}
