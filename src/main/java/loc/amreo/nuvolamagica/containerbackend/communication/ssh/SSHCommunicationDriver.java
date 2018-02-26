package loc.amreo.nuvolamagica.containerbackend.communication.ssh;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import loc.amreo.nuvolamagica.containerbackend.CommunicationDriver;

@Service
@ConditionalOnExpression("'${nuvolamagica.communication.driver:SSH}'=='SSH'")
public class SSHCommunicationDriver implements CommunicationDriver {

}
