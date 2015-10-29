package FileServer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/** Note that this class's input variable is not closed after every call in this class, 
    because the input is a data field and maybe used by httpGet after used by httpHEAD method.
    It will improve the efficiency by reading a file one time.
    But in request handler, a finally clause was added to set the FileServer object to null 
    after an process request method ends. It aims to set the used FileServer to garbage, and then 
    the input data field will automatically to be closed.*/
public class FileServer implements IServe{
	private final static String CRLF = "\r\n";
	private byte[] response;
	private File file;
	private FileInputStream input;
	private String phpOutput;

	public byte[] httpGet(String requestURI) throws HTTPFileNotFoundException, HTTPRuntimeException,
	HTTPPermissionDeniedException{
		byte[] header = httpHEAD(requestURI);
		
		//Create a ByteArrayOutputStream to add the header and entity-body together safely
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

		try{
			if (requestURI.endsWith(".php")){
				byteOutput.write(header);
				byteOutput.write(phpOutput.getBytes());
			}
			else{
				byteOutput.write(header);
				sendBytes(input, byteOutput);
				input.close();
			}
			
			response = byteOutput.toByteArray();
			byteOutput.close();	
			
		/* This IOException is just for transform input stream to byte output stream, since the input 
			has been checked in httpHEAD(requestURI), so this exception will never happen*/
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public byte[] httpGETconditional(String requestURI, Date ifModifiedSince) throws HTTPFileNotFoundException,
	HTTPRuntimeException, HTTPPermissionDeniedException{
		httpHEAD(requestURI);

		//Compare the ifModifiedSince with current time and file's last modified time
		if (ifModifiedSince.getTime() > new Date().getTime() || ifModifiedSince.getTime() < file.lastModified())
			return httpGet(requestURI);
		else 
			return "HTTP/1.0 304 Not Modified".getBytes();
	}

	public byte[] httpHEAD(String requestURI) throws HTTPFileNotFoundException, HTTPRuntimeException,
	HTTPPermissionDeniedException{
		String fileName = "." + "/webfiles" + requestURI;
		file = new File(fileName);
		int contentLength = 0;
		String statusLine = null;
		String date = null;
		String lastModified = null;
		String contentTypeLine = null;
		String contentLengthLine = "error";
		statusLine = "HTTP/1.0 200 OK" + CRLF ;

		//Check the whether the file is PHP file or not
		if (requestURI.endsWith(".php")){
			phpOutput = executePHP(fileName);
			contentLength = phpOutput.length();
		}
		else{
			try{
				input = new FileInputStream(file) ;	
				contentLength = input.available();
			} catch (FileNotFoundException ex ){
				
				// When file is not found, throw HTTPFileNotFoundException
				throw new HTTPFileNotFoundException("404 Not Found error", ex) ;
			} catch (IOException e) {
				
				// When file can not be read, throw HTTPPermissionDeniedException
				throw new HTTPPermissionDeniedException("File " + fileName + " cannot be read");
			}
		}

		statusLine = "HTTP/1.0 200 OK" + CRLF ;
		date = "Date: " + new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).format(new Date())+ CRLF ;
		lastModified = "Last-Modified: " + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH)
		.format(new Date(file.lastModified())) + CRLF ;
		contentTypeLine = "Content-type: " + contentType(fileName) + CRLF ;
		contentLengthLine = "Content-Length: " + 
				(new Integer(contentLength)).toString() + CRLF;
		
		//Add the headers together, and then send it to bytes
		response = (statusLine + date + contentTypeLine + lastModified +
				contentLengthLine + CRLF).getBytes();

		return response;
	}

	public byte[] httpPOST(String requestURI, byte[] postData) throws HTTPFileNotFoundException, HTTPRuntimeException,
	HTTPPermissionDeniedException{
		String fileName = "." + "/webfiles" + requestURI;
		file = new File(fileName);
		
		//Check the whether the file exists or not
		if (file.exists())
			return httpGet(requestURI);
		else{
			try {
				FileOutputStream fileOutput = new FileOutputStream(fileName);
				fileOutput.write(postData);
				fileOutput.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return "HTTP/1.0 201 Created".getBytes();
		}
	}

	/** Transform the data form file input stream to byte output stream */
	private static void sendBytes(FileInputStream fis, ByteArrayOutputStream os)
			throws IOException{
		
		// create 1K buffer
		byte[] buffer = new byte[1024] ;
		int bytes = 0 ;
		
		while ((bytes = fis.read(buffer)) != -1 ){
			os.write(buffer, 0, bytes);
		}
	}
	
	/** Get the file's corresponding content type */
	private static String contentType(String fileName){
		if (fileName.endsWith(".php"))
			return "text/html";
		else if(fileName.endsWith(".css"))
			return "text/css";
		else if(fileName.endsWith(".png"))
			return "image/png";
		else
			return "text/html";
	}	
	
	/** Execute a PHP file */
	public String executePHP(String scriptName) throws HTTPRuntimeException{
		StringBuilder output = new StringBuilder();
		BufferedReader input = null;
		String phpPath = "C:/PHP/php.exe";

		String line;
		Process process;
		try {
			
			// Process the PHP file
			process = Runtime.getRuntime().exec(phpPath + " " + scriptName);
			input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = input.readLine()) != null) {
				output.append(line+"\n");
			}

			if(line == null){
				process.destroy();
			}
		} catch (IOException e) {
			
			// If the file can not be executed, throw an HTTPRuntimeException
			throw new HTTPRuntimeException("File " +  scriptName + " did not execute correctly");
		}
		finally{
			if (input != null){
				try{
					input.close();
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}
		}
		return output.toString();
	}	
}
