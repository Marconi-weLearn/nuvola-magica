package loc.amreo.nuvolamagica.containerbackend.communication.mock;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.CommunicationDriver;

@Service
@ConditionalOnExpression("'${nuvolamagica.communication.driver}'=='mock'")
public class MockCommunicationDriver implements CommunicationDriver {

	@Override
	public void uploadFile(String communicationEndpoint, String filename, byte[] content) {
		System.out.println("[MockCommunicationDriver][uploadFile] I think I've uploaded the file " + filename + " with content: " + new String(content, StandardCharsets.UTF_8) + " to " + communicationEndpoint);
	}

	@Override
	public void deleteFile(String communicationEndpoint, String filename) {
		System.out.println("[MockCommunicationDriver][deleteFile] I think I've deleted the file " + filename + " to " + communicationEndpoint);
	}

	@Override
	public boolean existFile(String communicationEndpoint, String filename) {
		System.out.println("[MockCommunicationDriver][existFile] I think I've checked the file " + filename + " to " + communicationEndpoint + " and I think it exist");
		return true; //I don't know why but the file always exist.
	}

	@Override
	public byte[] getFile(String communicationEndpoint, String filename) {
		System.out.println("[MockCommunicationDriver][getFile] I think I've downloaded the file " + filename + " from " + communicationEndpoint);	
		return "The secret content".getBytes(); //I don't know why the content of the file is always this. Maybe I return a constant?
	}
}
