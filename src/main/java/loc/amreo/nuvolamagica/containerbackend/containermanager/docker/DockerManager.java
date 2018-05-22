package loc.amreo.nuvolamagica.containerbackend.containermanager.docker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;

import loc.amreo.nuvolamagica.containerbackend.ContainerManager;

@Service
@ConditionalOnExpression("'${nuvolamagica.container.manager:docker}'=='docker'")
public class DockerManager implements ContainerManager {

	//It contain the name of the image that will be used to create the containers
	private final String CONTAINER_IMAGE;
	private final String BEM_NETWORK_NAME;
	private final DockerClient dockerClient;
	private final Logger logger;
	
	public DockerManager(@Value("${nuvolamagica.container.manager.image:nuvola-magica-bem}") String CONTAINER_IMAGE,
			@Value("${nuvolamagica.container.manager.network:nuvola-magica-bem-network}") String BEM_NETWORK_NAME,
			@Autowired DockerClient dockerClient) {
		super();
		this.CONTAINER_IMAGE = CONTAINER_IMAGE;
		this.dockerClient = dockerClient;
		this.BEM_NETWORK_NAME = BEM_NETWORK_NAME;
		this.logger = Logger.getLogger(DockerManager.class);
	}

	@Override
	public String createContainer(String containerName) throws Exception {
		//Create the container
		dockerClient.createContainerCmd(CONTAINER_IMAGE)
				.withName(containerName)
				.withPublishAllPorts(true)
				.withNetworkMode(BEM_NETWORK_NAME)
				.exec();
		dockerClient.startContainerCmd(containerName).exec();
		//Get the infos
		InspectContainerResponse info = dockerClient.inspectContainerCmd(containerName).exec();
		String ip = info.getNetworkSettings().getNetworks().get(BEM_NETWORK_NAME).getIpAddress();
		//Logging
		logger.info("Created container name=" + containerName + " endpoint=" + ip + ":22");
		//Return the endpoint of the container
		return ip + ":22";
	}

	@Override
	public void pause(String containerName) throws Exception {
		dockerClient.pauseContainerCmd(containerName).exec();
		logger.info("Paused container name=" + containerName);
	}

	@Override
	public void unpause(String containerName) throws Exception {
		dockerClient.unpauseContainerCmd(containerName).exec();
		logger.info("Unpaused container name=" + containerName);
	}

	@Override
	public void destroy(String containerName) throws Exception {
		dockerClient.removeContainerCmd(containerName).withForce(true).exec();
		logger.info("Destroyed container name=" + containerName);
	}

}
