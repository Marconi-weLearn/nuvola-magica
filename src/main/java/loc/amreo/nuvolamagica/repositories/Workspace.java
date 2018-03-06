package loc.amreo.nuvolamagica.repositories;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Workspace {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;

	public Workspace(UUID id) {
		super();
		this.id = id;
	}
	public Workspace() {
		super();

	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
}
