package loc.amreo.nuvolamagica.controllers.frontendcommandsobject;

public class NewSessionResponse {
	private String sessionID;

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public NewSessionResponse() {
		super();
	}
	public NewSessionResponse(String sessionID) {
		super();
		this.sessionID = sessionID;
	}
	
}
