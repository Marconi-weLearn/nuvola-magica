package loc.amreo.nuvolamagica.containerbackend.communication.ssh;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.CommunicationDriver;

@Service
@ConditionalOnExpression("'${nuvolamagica.communication.driver:SSH}'=='SSH'")
public class SSHCommunicationDriver implements CommunicationDriver {

	@Override
	public void uploadFile(String communicationEndpoint, String filename, byte[] content) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFile(String communicationEndpoint, String filename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existFile(String communicationEndpoint, String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] getFile(String communicationEndpoint, String filename) {
		// TODO Auto-generated method stub
		return null;
	}

}
