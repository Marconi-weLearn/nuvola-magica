package loc.amreo.nuvolamagica.controllers;

import java.io.InputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import loc.amreo.nuvolamagica.services.FilesystemService;

@RestController
@RequestMapping("/api")
public class FilesystemRestController {
	
	@Autowired
	private FilesystemService filesystemService;
	
	@PutMapping(path="/workspace/{workspaceID}/sessions/{sessionID}/files/**")
	public ResponseEntity<Void> putFile(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, HttpServletRequest request, InputStream stream) throws Exception {
		//Get file name
		String filename = new AntPathMatcher()
	            .extractPathWithinPattern( "/workspace/{workspaceID}/sessions/{sessionID}/files/**", request.getRequestURI() );
		//I don't know why but the filename start with files/.
		filename = filename.replaceFirst("files/", "");
		//Get file content
		byte[] content = IOUtils.toByteArray(stream)
				;
		//Upload the file
		if (filesystemService.put(workspaceID, sessionID, filename, content)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping(path="/workspace/{workspaceID}/sessions/{sessionID}/files/**")
	public ResponseEntity<Void> deleteFile(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, HttpServletRequest request) throws Exception {
		//Get file name
		String filename = new AntPathMatcher()
	            .extractPathWithinPattern( "/workspace/{workspaceID}/sessions/{sessionID}/files/**", request.getRequestURI() );
		//I don't know why but the filename start with files/.
		filename = filename.replaceFirst("files/", "");
		//Delete the file
		if (filesystemService.delete(workspaceID, sessionID, filename)) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping(path="/workspace/{workspaceID}/sessions/{sessionID}/files/**")
	public ResponseEntity<byte[]> getFile(@PathVariable("workspaceID") UUID workspaceID, @PathVariable("sessionID") UUID sessionID, HttpServletRequest request) throws Exception {
		//Get file name
		String filename = new AntPathMatcher()
	            .extractPathWithinPattern( "/workspace/{workspaceID}/sessions/{sessionID}/files/**", request.getRequestURI() );
		//I don't know why but the filename start with files/.
		filename = filename.replaceFirst("files/", "");
		//Delete the file
		if (filesystemService.exist(workspaceID, sessionID, filename)) {
			return ResponseEntity.ok(filesystemService.get(workspaceID, sessionID, filename));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
