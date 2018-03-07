package loc.amreo.nuvolamagica.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;
import loc.amreo.nuvolamagica.services.BuildService;

@RestController
public class BuildAndExecuteRestController {

	@Autowired
	private BuildService buildService;
	
	@PostMapping("/api/workspace/{workspaceID}/sessions/{sessionID}/compilation")
	public ResponseEntity<CompilationResponse> compile(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, @RequestBody CompilationRequest request) {
		Optional<CompilationResponse> out = buildService.build(workspaceID, sessionID, request);
		
		return out
				.map(cr -> ResponseEntity.ok(cr))
				.orElseGet(() -> ResponseEntity.notFound().build());
		
	}
}
