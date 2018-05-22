package loc.amreo.nuvolamagica.containerbackend.containermanager.docker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;

import loc.amreo.nuvolamagica.containerbackend.ContainerManager;

@Service
@ConditionalOnExpression("'${nuvolamagica.container.manager:docker}'=='docker'")
public class DockerManager implements ContainerManager {

	//It contain the name of the image that will be used to create the containers
	private final String CONTAINER_IMAGE;
	private final String COMMUNICATION_ENDPOINT_IP;
	private final String BEM_NETWORK_NAME;
	private final DockerClient dockerClient;
	
	
	public DockerManager(@Value("${nuvolamagica.container.manager.image:nuvola-magica-bem}") String CONTAINER_IMAGE,
			@Value("${nuvolamagica.container.manager.communication-endpoint-ip:127.0.0.1}") String COMMUNICATION_ENDPOINT_IP,
			@Value("${nuvolamagica.container.manager.network:nuvola-magica-bem-network}") String BEM_NETWORK_NAME,
			@Autowired DockerClient dockerClient) {
		super();
		this.CONTAINER_IMAGE = CONTAINER_IMAGE;
		this.COMMUNICATION_ENDPOINT_IP = COMMUNICATION_ENDPOINT_IP;
		this.dockerClient = dockerClient;
		this.BEM_NETWORK_NAME = BEM_NETWORK_NAME;
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
		String port = info.getNetworkSettings().getPorts().getBindings().get(new ExposedPort(22))[0].getHostPortSpec();
		
		//Return the endpoint of the container
		return COMMUNICATION_ENDPOINT_IP + ":" + port;
	}

	@Override
	public void pause(String containerName) throws Exception {
		dockerClient.pauseContainerCmd(containerName).exec();
	}

	@Override
	public void unpause(String containerName) throws Exception {
		dockerClient.unpauseContainerCmd(containerName).exec();
	}

	@Override
	public void destroy(String containerName) throws Exception {
		dockerClient.removeContainerCmd(containerName).withForce(true).exec();
	}

}
