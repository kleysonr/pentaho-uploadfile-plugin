package br.gov.go.saude.pentaho.uploadfile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class FileSystem {

	/**
	 * Renomear um arquivo ou diretorio.
	 * 
	 * @param oldName
	 * @param newName
	 * @return true / false
	 */
	public static boolean renameFile(String oldName, String newName) {
		
		String fileTemp = null;

		// File (or directory) with old name
		File fileOld = new File(oldName);

		// File (or directory) with new name
		File fileNew = new File(newName);

		// Se o arquivo de destino ja existe, renomear para um nome temporario
		if (fileNew.exists()) {
			
			fileTemp = newName + ".tmp";

			if (!renameFile(newName, fileTemp)) {
				return false;
			}
		}

		// Rename file (or directory)
		boolean success = fileOld.renameTo(fileNew);
		if (!success) {
			return false;
			// File was not successfully renamed
		}

		// Se foi criado um arquivo temporario, apagar
		if (fileTemp != null) {
			boolean successDel = deleteFile(newName + ".tmp");
		}

		return true;
	}

	/**
	 * Deletar um arquivo
	 * 
	 * @param fileName
	 * @return true / false
	 */
	public static boolean deleteFile(String fileName) {

		boolean success = (new File(fileName)).delete();

		if (!success) {
			return false;
		}

		return true;
	}
	
	/**
	 * Incluir um texto no final de arquivos .txt
	 * 
	 * @param fileName
	 * @param texto
	 * @return true / false
	 */
	public static boolean appendTextToFile (String fileName, String texto) {
		
		FileWriter out = null;
		
		try {
			out = new FileWriter(fileName, true);
			out.write(texto);
			
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {		
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Ler o conteudo de um arquivo .txt
	 * 
	 * @param nomeArquivo
	 * @return string
	 */
	public static String lerArquivoTexto(String nomeArquivo) {

		File arquivo = new File(nomeArquivo);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(arquivo);
			
			byte[] bytes = new byte[fis.available()];
			fis.read(bytes);

			String conteudo = new String(bytes).replaceAll("\r", "");
			
			return conteudo;
		
		}
		catch (FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("Class lerArquivo: File not Found!");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (fis != null) {
				try {
					fis.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;		
	}
	
	/**
	 * Separa nome e extensao de um arquivo
	 * 
	 * @param nomeArquivo
	 * @return String[0] - nome do arquivo / String[1] - extensao do arquivo
	 */
	public static String[] splitFileName(String nomeArquivo) {

		String[] conteudo = new String[2];
		
		conteudo[0] = nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")); 
		conteudo[1] = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1);
		
		return conteudo;
	}
	
	public static void writeToFile(InputStream inputStream, String directory, String fileName) throws Exception 
	{
		File dir = new File(directory);
		if (!dir.exists()) dir.mkdirs();
		
		File file = new File(directory + File.separator + fileName);
		file.createNewFile();
		
		OutputStream out = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = inputStream.read(bytes)) != -1) 
		{
			out.write(bytes, 0, read);
		}
		
		out.flush();
		out.close();
	}


}
