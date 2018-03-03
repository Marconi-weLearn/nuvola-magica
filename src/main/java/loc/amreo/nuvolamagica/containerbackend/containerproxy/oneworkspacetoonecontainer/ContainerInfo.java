package loc.amreo.nuvolamagica.containerbackend.containerproxy.oneworkspacetoonecontainer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ContainerInfo {

	@Id
	private String containerName;
	private String communicationEndpoint;
	@ElementCollection
	private Set<UUID> sessions;
	
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public String getCommunicationEndpoint() {
		return communicationEndpoint;
	}
	public void setCommunicationEndpoint(String communicationEndpoint) {
		this.communicationEndpoint = communicationEndpoint;
	}
	public Set<UUID> getSessions() {
		return sessions;
	}
	public void setSessions(Set<UUID> sessions) {
		this.sessions = sessions;
	}
	public void addSession(UUID session) {
		this.sessions.add(session);
	}
	public void removeSession(UUID session) {
		this.sessions.remove(session);
	}
	
	public ContainerInfo() {
		super();
		this.sessions = new HashSet<UUID>();
	}
}
