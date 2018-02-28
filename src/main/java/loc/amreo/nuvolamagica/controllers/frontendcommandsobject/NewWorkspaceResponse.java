package loc.amreo.nuvolamagica.controllers.frontendcommandsobject;

public class NewWorkspaceResponse {
	private String workspaceID;

	public String getWorkspaceID() {
		return workspaceID;
	}

	public void setWorkspaceID(String workspaceID) {
		this.workspaceID = workspaceID;
	}

	public NewWorkspaceResponse(String workspaceID) {
		super();
		this.workspaceID = workspaceID;
	}
	public NewWorkspaceResponse() {
		super();
	}
	
}
