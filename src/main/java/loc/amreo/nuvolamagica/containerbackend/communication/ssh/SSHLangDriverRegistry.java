package loc.amreo.nuvolamagica.containerbackend.communication.ssh;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnBean(SSHCommunicationDriver.class)
public class SSHLangDriverRegistry {

}
