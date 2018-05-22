package loc.amreo.nuvolamagica.controllers.frontendcommandsobject;

import net.schmizz.sshj.connection.channel.direct.Signal;

public class SignalProcessRequest {
	private Signal signal;

	public Signal getSignal() {
		return signal;
	}

	public void setSignal(Signal signal) {
		this.signal= signal;
	}
	
}
