package loc.amreo.nuvolamagica.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.CompilationResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionRequest;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ExecutionResponse;
import loc.amreo.nuvolamagica.controllers.frontendcommandsobject.ProcessStatusResponse;
import loc.amreo.nuvolamagica.services.BuildService;
import loc.amreo.nuvolamagica.services.ExecutionService;

@RestController
public class BuildAndExecuteRestController {

	@Autowired
	private BuildService buildService;
	@Autowired
	private ExecutionService executionService;
	
	@PostMapping("/api/workspace/{workspaceID}/sessions/{sessionID}/compilation")
	public ResponseEntity<CompilationResponse> compile(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, @RequestBody CompilationRequest request) {
		Optional<CompilationResponse> out = buildService.build(workspaceID, sessionID, request);
		
		return out
				.map(cr -> ResponseEntity.ok(cr))
				.orElseGet(() -> ResponseEntity.notFound().build());
		
	}
	
	@PostMapping("/api/workspace/{workspaceID}/sessions/{sessionID}/processes")
	public ResponseEntity<ExecutionResponse> startExecution(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, @RequestBody ExecutionRequest request) {
		Optional<UUID> out = executionService.startProcess(workspaceID, sessionID, request);
		
		return out
				.map(er -> ResponseEntity.ok(new ExecutionResponse("/api/workspace/"+workspaceID+"/sessions/"+sessionID + "/processes/"+er)))
				.orElseGet(() -> ResponseEntity.notFound().build());	
	}
	
	@GetMapping("/api/workspace/{workspaceID}/sessions/{sessionID}/processes/{processID}/stdout")
	public ResponseEntity<byte[]> getProcessStdout(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, @PathVariable("processID") UUID processID) {
		Optional<byte[]> out = executionService.pullProcessStdout(workspaceID, sessionID, processID);
		
		return out
				.map(stdout -> ResponseEntity.ok(stdout))
				.orElseGet(() -> ResponseEntity.notFound().build());	
	}
	
	@GetMapping("/api/workspace/{workspaceID}/sessions/{sessionID}/processes/{processID}/stderr")
	public ResponseEntity<byte[]> getProcessStderr(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, @PathVariable("processID") UUID processID) {
		Optional<byte[]> err = executionService.pullProcessStderr(workspaceID, sessionID, processID);
		
		return err
				.map(stderr -> ResponseEntity.ok(stderr))
				.orElseGet(() -> ResponseEntity.notFound().build());	
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("/api/workspace/{workspaceID}/sessions/{sessionID}/processes/{processID}/stdin")
	public ResponseEntity pushProcessStdin(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, @PathVariable("processID") UUID processID, InputStream stream) throws IOException {
		//Get stream content
		byte[] content = IOUtils.toByteArray(stream);
		
		if (executionService.pushProcessStdin(workspaceID, sessionID, processID, content)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/api/workspace/{workspaceID}/sessions/{sessionID}/processes/{processID}/status")
	public ResponseEntity<ProcessStatusResponse> getProcessStatus(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, @PathVariable("processID") UUID processID) {
		return executionService.getProcessStatus(workspaceID, sessionID, processID)
				.map(status -> ResponseEntity.ok(status))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
