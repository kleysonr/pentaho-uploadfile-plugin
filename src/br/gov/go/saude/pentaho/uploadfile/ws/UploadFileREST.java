package br.gov.go.saude.pentaho.uploadfile.ws;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Saúde do Estado de Goiás<br>
 *         www.saude.go.gov.br
 *         
 * @author Marcello Pontes<br>
 * 		   www.oncase.com.br
 *
 */

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.pentaho.platform.api.engine.IPluginManager;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;

import br.gov.go.saude.pentaho.uploadfile.models.Output;
import br.gov.go.saude.pentaho.uploadfile.util.FileSystem;
import br.gov.go.saude.pentaho.uploadfile.util.Tokens;
import br.gov.go.saude.pentaho.uploadfile.util.TokensList;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("/uploadfile/api")
public class UploadFileREST {
	
	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	
	private String _DIRECTORY = "../../pentaho-solutions/system/uploadfile/resources/";
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/send")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	public Output uploadFile(
			@Context UriInfo info,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("prefix") String prefix, 
			@FormDataParam("solution") String solution, 
			@FormDataParam("description") String description, 
			@FormDataParam("newExt") String newExt) {
		
			// For simple CORS requests, the server only needs to add these 2 header parameters that allow access to any client.
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Credentials", "true");

			Output output = new Output();			
		
			// No file selected
			if ( "".equalsIgnoreCase(fileDetail.getFileName())  || fileDetail.getFileName() == null ) 
			{
				output.setMessage("No file selected.");
				output.setError(true);
				output.setError_message("UploadFile: ERROR");
			}
			else
			{
				// Get Auth Query Parameters
				String myType = "";
				myType = info.getQueryParameters().getFirst("type");
		
				String myToken = "";
				myToken = info.getQueryParameters().getFirst("token");
				
				String myUrlEncoded = "";
				myUrlEncoded = info.getQueryParameters().getFirst("urlEncoded");
				
				// If type/token/urlEncoded are defined, first do an authentication using the integrator
				try 
				{
					if ( !("".equalsIgnoreCase(myType)) && !(myType == null) && !("undefined".equalsIgnoreCase(myType)) && !("".equalsIgnoreCase(myToken)) && !(myToken == null) && !("undefined".equalsIgnoreCase(myToken)) && !("".equalsIgnoreCase(myUrlEncoded)) && !(myUrlEncoded == null) && !("undefined".equalsIgnoreCase(myUrlEncoded)) )
					{
						// Integrator Authenticate method by reflection
					 	IPluginManager plugMan = PentahoSystem.get(IPluginManager.class, PentahoSessionHolder.getSession());
		
					 	Object authBean = plugMan.getBean("integrator.auth");
					 	Method authenticate = authBean.getClass().getMethod("authenticate", HttpServletRequest.class, HttpServletResponse.class, UriInfo.class, String.class, String.class, String.class);			
						
						// Call Integrator Authenticate Method  info.getPath()
					 	Map<String, Object> ret = (Map<String, Object>) authenticate.invoke(authBean, request, response, info, myType, myToken, myUrlEncoded);
		
						// Authentication Success
						if ((boolean) ret.get("ok")) 
						{
							doUpload(info, uploadedInputStream, fileDetail, prefix, solution, description, newExt, output);
						}
						else
						{
							output.setMessage("Authentication failed.");
							output.setError(true);
							output.setError_message((String) ret.get("message"));
						}
					}
					
					// User is already logged. Do upload.
					else if (!("anonymousUser".equalsIgnoreCase(PentahoSessionHolder.getSession().getName())))
					{
						doUpload(info, uploadedInputStream, fileDetail, prefix, solution, description, newExt, output);
					}
					
					// Anonymous user
					else 
					{
						output.setMessage("Login required to upload files.");
						output.setError(true);
						output.setError_message("UploadFile: Error.");
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					output.setMessage("UploadFile: Internal Server Error.");
					output.setError(true);
					output.setError_message(e.getMessage());
				}
			}
			
			return output;
			
	}

	private void doUpload(UriInfo info, InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String prefix, String solution, String description, String newExt, Output output) throws Exception 
	{
		String directory = _DIRECTORY;

		// Set default name
		String fileName = fileDetail.getFileName();

		// If a new extension is defined, a new name based on the date will be defined to the file too
		if ( !("".equalsIgnoreCase(newExt)) && !(newExt == null) && !("undefined".equalsIgnoreCase(newExt)) )
		{
			Date dNow = new Date( );
			SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmmss"); // yyyy/MM/dd  HH:mm:ss

			fileName = ft.format(dNow).toString() + newExt;
		}
		
		// Add a prefix to the fileName if defined
		if ( !("".equalsIgnoreCase(prefix)) && !(prefix == null) && !("undefined".equalsIgnoreCase(prefix)) ) 
		{
			fileName = prefix + fileName;
		}
		output.setFile(fileName);
		
		
		// Define local storage
		if ( "".equalsIgnoreCase(solution) || solution == null || "undefined".equalsIgnoreCase(solution) )  
		{
			// Public storage
			directory = directory + "public";

			URI fileUrl = URI.create( info.getBaseUri().resolve("../").toString() + "content/uploadfile/resources/" + output.getFile() );
			
			output.setUrl(fileUrl.toString());
		}
		else 
		{
			// Private storage
			directory = directory + solution;
		}
		
		// Write to file
		FileSystem.writeToFile(uploadedInputStream, directory, fileName);
		
		// Write description to file
		if ( !("".equalsIgnoreCase(description)) && !(description == null) && !("undefined".equalsIgnoreCase(description)) ) 
		{
			
			// Replace tokens
			Tokens tokens = new Tokens();
			description = tokens.replace(description, TokensList.getMap());
			
			// Set file path
			String fileDescName = directory + File.separator + FileSystem.splitFileName(fileName)[0] + ".desc";
			
			// Write to file
			PrintWriter outDesc = new PrintWriter(new FileWriter(fileDescName));
			outDesc.println(description);
			outDesc.close();
		}

	}
		
}