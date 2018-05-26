package loc.amreo.nuvolamagica.containerbackend.communication.ssh.sshlangdrivers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import loc.amreo.nuvolamagica.containerbackend.communication.ssh.SSHLangDriverRegistry;

@Component
@ConditionalOnBean(SSHLangDriverRegistry.class)
public class JavaSSHLangDriver implements SSHLangDriver {

	public JavaSSHLangDriver(@Autowired SSHLangDriverRegistry registry) {
		registry.registerDriver("java", this);
	}
	
	@Override
	public String getBuildCommand(String mainFile, String options) {
		return "javac " + options + " " + mainFile;
	}

	public String getExecuteCommand(String filename, String options) {
		return "java " + options + " " + filename.replace(".java", "") ;
	}

}
