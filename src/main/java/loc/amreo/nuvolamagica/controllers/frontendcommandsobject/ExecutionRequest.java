package loc.amreo.nuvolamagica.controllers.frontendcommandsobject;

public class ExecutionRequest {
	String chrootDir;
	String langType;
	String options;
	String mainFile;
	
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
	public String getMainFile() {
		return mainFile;
	}
	public void setMainFile(String mainFile) {
		this.mainFile = mainFile;
	}
}
