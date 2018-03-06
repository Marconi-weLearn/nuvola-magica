package loc.amreo.nuvolamagica.containerbackend;


public interface ContainerManager {

	String createContainer(String containerName);
	void destroy(String containerName);
	
	void pause(String containerName);
	void unpause(String containerName);
}
