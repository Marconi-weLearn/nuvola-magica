package loc.amreo.nuvolamagica.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.ContainerProxy;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionRequest;

@Service
public class ExecutionService {

	@Autowired
	private SessionService sessionService;
	@Autowired
	private ContainerProxy containerProxy;
	
	public Optional<UUID> startProcess(UUID workspaceID, UUID sessionID, ExecutionRequest request) {
		if (sessionService.isSessionExisting(workspaceID, sessionID)) {
			return Optional.of(containerProxy.execute(workspaceID, sessionID, request));
		} else {
			return Optional.empty();
		}
	}

}
