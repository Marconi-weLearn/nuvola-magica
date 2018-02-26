package loc.amreo.nuvolamagica.containerbackend.communication.ssh.sshlangdrivers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import loc.amreo.nuvolamagica.containerbackend.communication.ssh.SSHLangDriverRegistry;

@ConditionalOnBean(SSHLangDriverRegistry.class)
public class JavaSSHLangDriver implements SSHLangDriver {

}
