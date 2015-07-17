package br.gov.go.saude.pentaho.uploadfile.engine;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Sa�de do Estado de Goi�s<br>
 *         www.saude.go.gov.br
 *
 */

import javax.sql.DataSource;

import org.pentaho.platform.api.data.IDBDatasourceService;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
public class UploadfileTemplateFactory implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException 
	{
		
		//initPluginProperties();

	}
	
}
