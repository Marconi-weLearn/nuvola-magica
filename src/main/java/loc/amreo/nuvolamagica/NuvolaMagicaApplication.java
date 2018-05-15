package loc.amreo.nuvolamagica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;


@SpringBootApplication
public class NuvolaMagicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NuvolaMagicaApplication.class, args);
	}
	
	@Bean
	public DockerClient dockerClient() {
		return DockerClientBuilder.getInstance().build();
	}
}
