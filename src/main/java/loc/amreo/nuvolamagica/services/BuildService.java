package loc.amreo.nuvolamagica.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.ContainerProxy;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;

@Service
public class BuildService {

		@Autowired
		private SessionService sessionService;
		@Autowired
		private ContainerProxy containerProxy;
		
		public Optional<CompilationResponse> build(UUID workspaceID, UUID sessionID, CompilationRequest request) throws Exception {
			if (sessionService.isSessionExisting(workspaceID, sessionID)) {
				return Optional.of(containerProxy.build(workspaceID, sessionID, request));
			} else {
				return Optional.empty();
			}
		}
}
