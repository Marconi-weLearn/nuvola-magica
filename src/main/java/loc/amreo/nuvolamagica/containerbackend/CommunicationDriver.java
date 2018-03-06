package loc.amreo.nuvolamagica.containerbackend;

public interface CommunicationDriver {

	void uploadFile(String communicationEndpoint, String filename, byte[] content);
	void deleteFile(String communicationEndpoint, String filename);
	boolean existFile(String communicationEndpoint, String filename);
	byte[] getFile(String communicationEndpoint, String filename);
}
