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
	
	public boolean put(UUID workspaceID, UUID sessionID, String filename, byte[] content) throws Exception {
		if (sessionService.isSessionExisting(workspaceID, sessionID)) {
			containerProxy.uploadFile(workspaceID, sessionID, filename, content);
			return true;
		} else {
			return false;
		}
	}

	public boolean delete(UUID workspaceID, UUID sessionID, String filename) throws Exception {
		if (sessionService.isSessionExisting(workspaceID, sessionID)) {
			containerProxy.deleteFile(workspaceID, sessionID, filename);
			return true;
		} else {
			return false;
		}
	}

	public boolean exist(UUID workspaceID, UUID sessionID, String filename) throws Exception {
		if (sessionService.isSessionExisting(workspaceID, sessionID)) {
			return containerProxy.existFile(workspaceID, sessionID, filename);
		} else {
			return false;
		}
	}

	public byte[] get(UUID workspaceID, UUID sessionID, String filename) throws Exception {
		//The method assume that it's called with valid workspaceID and sessionID, and the file exist
		return containerProxy.getFile(workspaceID, sessionID, filename);
	}

}
