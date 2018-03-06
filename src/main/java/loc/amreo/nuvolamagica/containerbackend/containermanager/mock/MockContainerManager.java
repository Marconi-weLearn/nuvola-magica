package loc.amreo.nuvolamagica.containerbackend.containermanager.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.ContainerManager;

@Service
@ConditionalOnProperty(name="nuvolamagica.container.manager", havingValue="mock")
public class MockContainerManager implements ContainerManager {

	@Override
	public String createContainer(String containerName) {
		System.out.println("[MockContainerManager][CreateContainer] I think I've created the container: " + containerName);
		return "127.0.0.1:" + containerName; //The TRUE RANDOM communication endpoint
	}

	@Override
	public void pause(String containerName) {
		System.out.println("[MockContainerManager][Pause] I think I've pauesed the container: " + containerName);
	}

	@Override
	public void unpause(String containerName) {
		System.out.println("[MockContainerManager][Unpause] I think I've unpauesed the container: " + containerName);
	}

	@Override
	public void destroy(String containerName) {
		System.out.println("[MockContainerManager][Destroy] I think I've destroyed the container: " + containerName);
	}

}
