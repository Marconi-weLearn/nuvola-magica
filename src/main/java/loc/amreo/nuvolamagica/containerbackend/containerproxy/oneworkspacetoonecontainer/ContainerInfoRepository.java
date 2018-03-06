package loc.amreo.nuvolamagica.containerbackend.containerproxy.oneworkspacetoonecontainer;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainerInfoRepository extends JpaRepository<ContainerInfo, String>  {

	public ContainerInfo findOneByContainerName(String containerName);
	@Transactional
	public void deleteAllByContainerName(String containerName);
}
