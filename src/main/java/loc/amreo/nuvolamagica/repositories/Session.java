package loc.amreo.nuvolamagica.repositories;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Session {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;
	private UUID workspaceID;
	private LocalDateTime creationDate;
	private LocalDateTime timeoutDate;
	
	public Session() {
		super();
		this.creationDate = LocalDateTime.now();
	}
	public Session(UUID id, UUID workspaceID,LocalDateTime creationDate, LocalDateTime timeoutDate) {
		super();
		this.id = id;
		this.workspaceID = workspaceID;
		this.creationDate = creationDate;
		this.timeoutDate = timeoutDate;
	}
	public Session(UUID id, UUID workspaceID, LocalDateTime timeoutDate) {
		super();
		this.id = id;
		this.workspaceID = workspaceID;
		this.creationDate = LocalDateTime.now();
		this.timeoutDate = timeoutDate;
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public LocalDateTime getTimeoutDate() {
		return timeoutDate;
	}
	public void setTimeoutDate(LocalDateTime timeoutDate) {
		this.timeoutDate = timeoutDate;
	}
	public UUID getWorkspaceID() {
		return workspaceID;
	}
	public void setWorkspaceID(UUID workspaceID) {
		this.workspaceID = workspaceID;
	}
	
}
