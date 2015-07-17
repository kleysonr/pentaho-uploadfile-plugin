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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.gov.go.saude.pentaho.uploadfile.models.Output;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("/uploadfile/api")
public class UploadFileREST {
	
	private String DIRECTORY = 
			"../../pentaho-solutions/system/uploadfile/";

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
		
			Output output = new Output();			
			
			// Define local storage
			if ( "".equalsIgnoreCase(solution) || solution == null ) 
			{
				// Public storage
				DIRECTORY = DIRECTORY + "resources";

				URI fileUrl = 
						URI.create(info.getBaseUri().resolve("../").toString()
						+ "content/uploadfile/resources/" + fileDetail.getFileName());
				
				output.setUrl(fileUrl.toString());
			}
			else 
			{
				// Private storage
				DIRECTORY = DIRECTORY + solution;
			}
			
			// Define output file name
			// If prefix and nexExt are defined a new name based on the date will be created
			String uploadedFileLocation = DIRECTORY + File.separator;
			String fileName = fileDetail.getFileName();
			
			if ("".equalsIgnoreCase(prefix) || prefix == null || "".equalsIgnoreCase(newExt) || newExt == null ) 
			{

			}
			else
			{
				Date dNow = new Date( );
				SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmmss"); // yyyy/MM/dd  HH:mm:ss

				fileName = prefix + ft.format(dNow).toString() + newExt;
			}
			uploadedFileLocation = uploadedFileLocation + fileName;
			output.setFile(fileName);
			
			
			// Write to file
			try {
				writeToFile(uploadedInputStream, uploadedFileLocation);
			} catch (Exception e) {
				output.setMessage(e.getMessage());
				String error_message = "";
				for( StackTraceElement elem : e.getStackTrace()) {
					error_message += elem.toString() + "\n";
				}
				output.setError(true);
				output.setError_message(error_message);
			}
	 
			return output;
			
		}
		
		// save uploaded file to new location
		private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException 
		{
			File dir = new File(DIRECTORY);
			dir.mkdirs();
			File file = new File(uploadedFileLocation);
			file.createNewFile();
			OutputStream out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = uploadedInputStream.read(bytes)) != -1) 
			{
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		}
}