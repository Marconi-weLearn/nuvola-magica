package loc.amreo.nuvolamagica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public NewWorkspaceResponse postWorkspace() {
		return new NewWorkspaceResponse("/api/workspace/"+workspaceService.createWorkspace().toString());
	}
}
