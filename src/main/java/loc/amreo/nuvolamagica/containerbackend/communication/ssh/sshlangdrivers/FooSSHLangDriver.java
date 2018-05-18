package loc.amreo.nuvolamagica.containerbackend.communication.ssh.sshlangdrivers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import loc.amreo.nuvolamagica.containerbackend.communication.ssh.SSHLangDriverRegistry;

@Component
@ConditionalOnBean(SSHLangDriverRegistry.class)
public class FooSSHLangDriver implements SSHLangDriver {

	public FooSSHLangDriver(@Autowired SSHLangDriverRegistry registry) {
		registry.registerDriver("foo", this);
	}
	
	@Override
	public String getBuildCommand(String mainFile, String options) {
		return String.format("bash -c \"echo #!/bin/bash echo %s %s >> out.sh && chmod +x out.sh \"", mainFile, options);
	}
}
