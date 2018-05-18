package loc.amreo.nuvolamagica.containerbackend.communication.ssh.sshlangdrivers;

public interface SSHLangDriver {
	
	//Return the command used for building the project
	public String getBuildCommand(String mainFile, String options);
}
