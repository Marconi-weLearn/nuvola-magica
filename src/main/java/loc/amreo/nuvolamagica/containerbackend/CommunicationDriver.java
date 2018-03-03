package loc.amreo.nuvolamagica.containerbackend;

public interface CommunicationDriver {

	void upload(String communicationEndpoint, String filename, byte[] content);
}
