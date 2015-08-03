package br.gov.go.saude.pentaho.uploadfile.util;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Saúde do Estado de Goiás<br>
 *         www.saude.go.gov.br
 *         
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.pentaho.platform.engine.core.system.PentahoSessionHolder;

/*
 * Tokens:
 * 
 * ${user} - Pentaho logged user
 * ${day} - Current day  
 * ${month} - Current month
 * ${year} - Current year
 * ${hh} - Current hour
 * ${mm} - Current minute
 * ${ss} - Current seconds
 * 
 */

public class TokensList {
	
	public static Map<String, String> getMap() {
		
		Map<String, String> tokensList = new HashMap<String, String>();;
		
		// Logged user
		tokensList.put("user", PentahoSessionHolder.getSession().getName());
		
		// Date and Time
		Date dNow = new Date( );

		SimpleDateFormat ft = new SimpleDateFormat ("dd");
		tokensList.put("day", ft.format(dNow).toString());

		ft = new SimpleDateFormat ("MM");
		tokensList.put("month", ft.format(dNow).toString());

		ft = new SimpleDateFormat ("yyyy");
		tokensList.put("year", ft.format(dNow).toString());

		ft = new SimpleDateFormat ("HH");
		tokensList.put("hh", ft.format(dNow).toString());

		ft = new SimpleDateFormat ("mm");
		tokensList.put("mm", ft.format(dNow).toString());

		ft = new SimpleDateFormat ("ss");
		tokensList.put("ss", ft.format(dNow).toString());

		return tokensList;
	}

}
