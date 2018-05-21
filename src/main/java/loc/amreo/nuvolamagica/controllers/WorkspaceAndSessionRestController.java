package loc.amreo.nuvolamagica.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.NewSessionResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.NewWorkspaceResponse;
import loc.amreo.nuvolamagica.services.SessionService;
import loc.amreo.nuvolamagica.services.WorkspaceService;

@RestController
@RequestMapping("/api")
public class WorkspaceAndSessionRestController {
	
	//Services required
	@Autowired
	private WorkspaceService workspaceService;
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/workspace")
	public NewWorkspaceResponse postWorkspace() throws Exception {
		return new NewWorkspaceResponse("/api/workspace/"+workspaceService.createWorkspace().toString());
	}
	
	@PostMapping("/workspace/{workspaceID}/sessions")
	public ResponseEntity<NewSessionResponse> postWorkspace(@PathVariable("workspaceID") UUID workspaceID) throws Exception {
		Optional<UUID> sessionID = sessionService.createSession(workspaceID);
	
		return sessionID
				.map(id -> ResponseEntity.ok(new NewSessionResponse("/api/workspace/"+workspaceID+"/sessions/"+id.toString()))
				).orElseGet(() -> ResponseEntity.notFound().build());
		
	}
	
	@GetMapping("/workspace/{workspaceID}/sessions/{sessionID}/wakeup")
	public ResponseEntity<String> getSessionWakeup(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID) throws Exception {
		if (sessionService.wakeUpSession(workspaceID, sessionID)) {
			return ResponseEntity.ok("I'M AWAKE!");
		} else {
			return ResponseEntity.notFound().build();
		}	
	}
	
	@DeleteMapping("/workspace/{workspaceID}/sessions/{sessionID}")
	public ResponseEntity<Void> deleteSession(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID) throws Exception {
		if (sessionService.deleteSession(workspaceID, sessionID)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}	
	}
	
	@DeleteMapping("/workspace/{workspaceID}")
	public ResponseEntity<Void> deleteWorkspace(@PathVariable("workspaceID") UUID workspaceID) throws Exception {
		if (workspaceService.deleteWorkspace(workspaceID)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
