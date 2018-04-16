package loc.amreo.nuvolamagica.controllers.frontendcommandsobject;

public class ExecutionResponse {
	private String processID;

	public String getProcessID() {
		return processID;
	}
	public void setProcessID(String processID) {
		this.processID = processID;
	}
	public ExecutionResponse(String processID) {
		super();
		this.processID = processID;
	}
	
}
