package loc.amreo.nuvolamagica.containerbackend.containermanager.docker;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import loc.amreo.nuvolamagica.containerbackend.ContainerManager;

@ConditionalOnExpression("'${nuvolamagica.container.manager:docker}'=='docker'")
public class DockerManager implements ContainerManager {

}
