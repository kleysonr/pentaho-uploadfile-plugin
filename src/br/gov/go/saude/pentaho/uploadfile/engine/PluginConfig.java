package br.gov.go.saude.pentaho.uploadfile.engine;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Saúde do Estado de Goiás<br>
 *         www.saude.go.gov.br
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.engine.core.system.PentahoSystem;

public class PluginConfig {
    
    public final static String PLUGIN_NAME = "uploadfile";
    public final static String PLUGIN_PATH = PentahoSystem.getApplicationContext().getSolutionPath("system" + File.separator + PLUGIN_NAME);
    public final static Properties props = new Properties();
    
    static Log logger = LogFactory.getLog(PluginConfig.class);
	  
	private static PluginConfig _instance;
	
	public PluginConfig() {
	}
	
	public static synchronized PluginConfig getInstance() 
	{
		if (_instance == null) {
			_instance = new PluginConfig();
		}
		return _instance;
	}
	
	public void init() 
	{
		loadPluginProperties();
	}

    private void loadPluginProperties() 
    {
        try 
        {
        	InputStream in = new FileInputStream(PLUGIN_PATH + File.separator + "uploadfile.properties");
            props.load(in);
        } 
        catch (IOException e) 
        {
        	System.out.println("UPLOADFILES_PLUGIN: Missing uploadfile.properties file.");
			e.printStackTrace();
		}
    }
}
