package br.gov.go.saude.pentaho.uploadfile.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Output {

	private String message;
	private String url;
	private String file;
	private Boolean error;
	private String error_message;
	
	public Output() {
		setMessage("File Uploaded");
		setUrl("Private");
		setFile("");
		setError(false);
		setError_message("");
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getFile() {
		return file;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public Boolean getError() {
		return error;
	}
	
	public void setError(Boolean error) {
		this.error = error;
	}
	
	public String getError_message() {
		return error_message;
	}
	
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	
}
