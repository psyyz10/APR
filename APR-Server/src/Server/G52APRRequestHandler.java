package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Observable;
import java.util.StringTokenizer;

import FileServer.FileServer;
import FileServer.HTTPFileNotFoundException;
import FileServer.HTTPPermissionDeniedException;
import FileServer.HTTPRuntimeException;


public class G52APRRequestHandler extends Observable implements Runnable{
	private final static String CRLF = "\r\n";
	private Socket socket;
	private BufferedInputStream input;
	private BufferedOutputStream output;
	private BufferedReader br;
	private byte[] response;
	private char loggerState;
	private String requestLine;
	private String statusLine;
	private String fileName;

	/** Constructor to make G52RequestHandler object with a socket. */
	public G52APRRequestHandler(Socket socket) throws Exception{
		this.socket = socket;
		this.input = new BufferedInputStream(socket.getInputStream());
		this.output = new BufferedOutputStream(socket.getOutputStream());
		this.br = new BufferedReader(new InputStreamReader(input));
		
		setLoggerState('$');
	}

	/** Run a thread */
	public void run()
	{
		try {
			response = processRequest();
			output.write(response);
		}
		catch(Exception e) {		
			e.printStackTrace();
		}
		finally{

			//Close the IO
			try{
				output.close();
				br.close();
				socket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Process the request() */
	private byte[] processRequest() throws Exception 
	{
		FileServer fileServer = new FileServer();
		String method = null;
		String requestURI = null;
		String headerName = null;
		String headerContent = null;
		String headerLine = null;
		HashMap<String, String> headers = new HashMap<String, String>();
		byte[] entityBody = null;
		byte[] response = null;
		String[] tokens = null;

		requestLine = br.readLine();
		StringTokenizer s = new StringTokenizer(requestLine);
		method = s.nextToken();
		requestURI = s.nextToken();
		fileName = requestURI;
		setLoggerState('>');

		while(true) {
			headerLine = br.readLine();

			//Stop the handle loop when request has been handled completely
			if(headerLine.equalsIgnoreCase(CRLF)||headerLine.equals("")){		
				break;
			}

			//Separate every element in every request line in tokens
			tokens = headerLine.split(" ", 2);
			headerName = tokens[0];
			headerContent = tokens[1];

			//Put headers into a map
			headers.put(headerName, headerContent);			
		}	

		// If headers contains Content-Length header, store the entity- body to entityBody variable
		if (headers.containsKey("Content-Length:")){
			int contentLength = Integer.parseInt(headers.get("Content-Length:"));
			entityBody = new byte [contentLength];
			for (int i = 0; i < contentLength; i++){
				entityBody[i] = (byte) br.read();
			}
		}

		//Check which request it is and invoke the corresponding method
		try{
			if (method.equalsIgnoreCase("GET") && headers.containsKey("If-Modified-Since:")){
				String date = headers.get("If-Modified-Since:");
				response = fileServer.httpGETconditional(requestURI, new SimpleDateFormat
						("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH).parse(date));
			}
			else if (method.equalsIgnoreCase("GET"))
				response = fileServer.httpGet(requestURI);
			else if (method.equalsIgnoreCase("HEAD"))
				response = fileServer.httpHEAD(requestURI);
			else if (method.equalsIgnoreCase("POST"))
				response = fileServer.httpPOST(requestURI, entityBody);
		}
		catch(HTTPFileNotFoundException ex){

			// If catch HTTPFileNotFoundException, return corresponding error message to client
			response = "HTTP/1.0 404 Not Found error\n".getBytes();
		}
		catch(HTTPRuntimeException ex){

			// If catch HTTPRuntimeException, return corresponding error message to client
			response = "HTTP/1.0 500 Internal Server error\n".getBytes();
		}
		catch(HTTPPermissionDeniedException ex){

			// If catch HTTPPermissionDeniedException, return corresponding error message to client
			response = "HTTP/1.0 403 Forbidden error\n".getBytes();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally {

			// Set the fileServer object to garbage, it means to close file's file input stream data field variable
			fileServer = null;
		}
		
		statusLine = new String(response);
		statusLine = statusLine.substring(0, statusLine.indexOf("\n"));
		setLoggerState('<');
		
		return response;
	}
	
	public String getFileName(){
		return fileName;
	}
	
	public String getStatusLine(){
		return statusLine;
	}

	public String getRequestLine(){
		return requestLine;
	}

	public char getLoggerState(){
		return loggerState;
	}

	/* When the setLoggerState method is invoked, the request handler will notify the loggers */
	public void setLoggerState(char loggerState){
		this.loggerState = loggerState;
		setChanged();
		notifyObservers();
	}
}