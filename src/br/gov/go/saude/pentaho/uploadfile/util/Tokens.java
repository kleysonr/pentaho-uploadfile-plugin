package br.gov.go.saude.pentaho.uploadfile.util;

import java.util.Map;

public class Tokens {
	
	// Replacing ${variable} style tokens
	public String replace(String template, Map<String, String> tokensList)
	{
		String s = template;
		for (Map.Entry<String, String> e : tokensList.entrySet()) {
		  s = s.replaceAll("\\$\\{(" + e.getKey() + ")\\}", e.getValue());
		}
		
		return s;
	}

}
