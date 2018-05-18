package loc.amreo.nuvolamagica.containerbackend.communication.ssh;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.communication.ssh.sshlangdrivers.SSHLangDriver;

@Service
@ConditionalOnBean(SSHCommunicationDriver.class)
public class SSHLangDriverRegistry {
	
	//<Language, Driver>
	private HashMap<String, SSHLangDriver> drivers;

	public SSHLangDriverRegistry() {
		drivers = new HashMap<>();
	}
	
	//Add the driver to the registry
	public void registerDriver(String lang, SSHLangDriver driver) {
		drivers.put(lang, driver);
	}
	
	public Optional<String> getBuildCommand(String lang, String filename, String options) {
		//Check if the lang exist
		if (drivers.containsKey(lang)) {
			return Optional.of(drivers.get(lang).getBuildCommand(filename, options));
		} else {
			return Optional.empty();
		}
	}
}
