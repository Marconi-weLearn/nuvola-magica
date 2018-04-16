package loc.amreo.nuvolamagica.controllers.frontendcommandsobject;

public class ProcessStatusResponse {
	private int exitCode;
	public String status;
	public int getExitCode() {
		return exitCode;
	}
	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ProcessStatusResponse(int exitCode, String status) {
		super();
		this.exitCode = exitCode;
		this.status = status;
	}
	
	
}
