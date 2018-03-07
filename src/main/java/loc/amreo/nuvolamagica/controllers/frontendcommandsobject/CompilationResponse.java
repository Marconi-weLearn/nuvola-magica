package loc.amreo.nuvolamagica.controllers.frontendcommandsobject;

public class CompilationResponse {
	int compilationResultCode;
	String compilationOutput;
	
	public int getCompilationResultCode() {
		return compilationResultCode;
	}
	public void setCompilationResultCode(int compilationResultCode) {
		this.compilationResultCode = compilationResultCode;
	}
	public String getCompilationOutput() {
		return compilationOutput;
	}
	public void setCompilationOutput(String compilationOutput) {
		this.compilationOutput = compilationOutput;
	}
	
	public CompilationResponse() {
		super();
	}
	public CompilationResponse(int compilationResultCode, String compilationOutput) {
		super();
		this.compilationResultCode = compilationResultCode;
		this.compilationOutput = compilationOutput;
	}
	
	
}
