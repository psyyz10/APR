package Server;

import java.io.*;
import java.net.*;
import java.util.HashSet;

import Logger.ILogger;


public class G52APRNetwork{

	public G52APRNetwork(HashSet<ILogger> loggers){
		try {
			//Create a server socket
			ServerSocket serverSocket = new ServerSocket(4444);

			while (true){
				//Listen for a new connection request
				Socket socket = serverSocket.accept();
				
				try {
					//Send the socket to request handler to handle the socket
					G52APRRequestHandler task = new G52APRRequestHandler(socket);
					
					//Add the loggers to observe request handler
					for (ILogger logger: loggers){
						task.addObserver(logger);
					}
					
					//Create and start a new thread for the connection
					new Thread(task).start();
				}
				catch(Exception e) {
					System.out.println(e);
				}
			}
		}catch (IOException ex){
			System.err.println(ex);
		}
	}
} 