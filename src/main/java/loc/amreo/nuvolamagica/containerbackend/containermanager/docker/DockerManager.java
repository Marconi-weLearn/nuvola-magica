package loc.amreo.nuvolamagica.containerbackend.containermanager.docker;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.ContainerManager;

@Service
@ConditionalOnExpression("'${nuvolamagica.container.manager:docker}'=='docker'")
public class DockerManager implements ContainerManager {

	@Override
	public String createContainer(String containerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pause(String containerName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unpause(String containerName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy(String containerName) {
		// TODO Auto-generated method stub
		
	}

}
