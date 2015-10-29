package Server;

import java.util.HashSet;

import Logger.FileLogger;
import Logger.ILogger;



/** 
 * The Logger part uses the Strategy and Observer patterns here.
 * 
 * Observer:
 * The Loggers are observers and observe the request handler which extends Observable.
 * When the corresponding process is done, the request will notify the loggers.
 * 
 * Strategy:
 * All the Loggers implement the ILogger interface.
 * How the server records its logs can be varied by creating a new implementation of the ILogger interface.
 * Multiple Loggers can be created and added to the ILogger hash set as a logger set.
 * All the Loggers which are observers in the ILogger hash set will be added to observe the request handler(observable).
 * 
*/
public class ServerController {
	public static void main(String[] args) {
		
		/* Use ILogger type hash set to store loggers so that multiple loggers can be added */
		HashSet<ILogger> loggers = new HashSet<ILogger>();
		int argsLength = args.length;
		
		if (argsLength > 2){
			System.out.println("The syntax of the command is incorrect");
			System.exit(0);
		}
		else if (argsLength == 0){
			loggers.add(new FileLogger("log.txt", false));
			new G52APRNetwork(loggers);
		}
		else if (argsLength == 1){
			if (!args[0].equals("-l")){
				System.out.println("ERROR: undefined parameter " + args[0]);
				System.exit(0);
			}
			else{
				loggers.add(new FileLogger("log.txt", false));
				new G52APRNetwork(loggers);
			}
		}
		else if (argsLength == 2){
			if ((args[0].equals("-l") && args[1].equals("-r")) || 
					(args[0].equals("-r") && args[1].equals("-l"))){
				loggers.add(new FileLogger("log.txt", true));
				new G52APRNetwork(loggers);
			}
			else{
				System.out.println("ERROR: undefined parameter " + args[0] + " " + args[1]);
				System.exit(0);
			}
		}
	}
}