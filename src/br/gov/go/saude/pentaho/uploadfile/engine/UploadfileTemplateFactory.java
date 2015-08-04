package br.gov.go.saude.pentaho.uploadfile.engine;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Saúde do Estado de Goiás<br>
 *         www.saude.go.gov.br
 *
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
public class UploadfileTemplateFactory implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException 
	{
		initPluginProperties();
	}
	
	private void initPluginProperties() 
	{
		PluginConfig.getInstance().init();	
	}

	
}
