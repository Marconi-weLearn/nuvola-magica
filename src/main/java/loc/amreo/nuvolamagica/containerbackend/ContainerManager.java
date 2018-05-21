package loc.amreo.nuvolamagica.containerbackend;


public interface ContainerManager {

	String createContainer(String containerName) throws Exception;
	void destroy(String containerName) throws Exception;
	
	void pause(String containerName) throws Exception;
	void unpause(String containerName) throws Exception;
}
