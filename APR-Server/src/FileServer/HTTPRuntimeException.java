package FileServer;


/**
 * This class provides a custom Exception that can be thrown if the file
 * specified by the Request-URI did not execute correctly. This Exception should
 * therefore only be thrown by an implementation of IServe that
 * executes files, and not by the simple file store, which should never execute
 * files.
 * 
 * Example usage: if (e.g.)\/cgi-bin\/myscript.sh does not execute
 * correctly:
 * 
 * throw new HTTPRuntimeException("File /cgi-bin/myscript.sh did not execute correctly");
 * 
 * You may include additional information if you wish by passing a
 * Throwable Object that caused the problem.
 * 
 * This might suggest a 500 Internal Server error.
 * 
 * @author G52APR
 * @since 2012/09/03
 * @version 1
 */
public class HTTPRuntimeException extends Exception {

	private static final long serialVersionUID = -4959685019033110083L;

	public HTTPRuntimeException(String message) {
		super(message);
	}

	public HTTPRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
