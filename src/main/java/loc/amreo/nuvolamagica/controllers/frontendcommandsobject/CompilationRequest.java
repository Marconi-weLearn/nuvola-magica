package loc.amreo.nuvolamagica.controllers.frontendcommandsobject;

public class CompilationRequest {
	String chrootDir;
	String langType;
	String options;
	
	
	public String getChrootDir() {
		return chrootDir;
	}
	public void setChrootDir(String chrootDir) {
		this.chrootDir = chrootDir;
	}
	public String getLangType() {
		return langType;
	}
	public void setLangType(String langType) {
		this.langType = langType;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
}
