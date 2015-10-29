package FileServer;


/**
 * This class provides a custom Exception that can be thrown if the file
 * specified by the Request-URI exists but cannot be accessed. This may mean
 * that the permissions on the file are set to disallow read access or execution
 * access.
 * <p>
 * Example usage: if (e.g.) <code>/cgi-bin/myscript.sh</code> does not have
 * execution permissions:
 * <p>
 * <code>throw new HTTPPermissionDeniedException("File /cgi-bin/myscript.sh cannot be executed");</code>
 * <p>
 * Example usage 2: if (e.g.) <code>/hello.html</code> does not have read
 * permissions:
 * <p>
 * <code>throw new HTTPPermissionDeniedException("File /hello.html cannot be read");</code>
 * <p>
 * You may include additional information if you wish by passing a
 * <code>Throwable</code> Object that caused the problem.
 * <p>
 * This might suggest a 403 Forbidden error.
 * 
 * @author G52APR
 * @since 2012/09/03
 * @version 1
 */
public class HTTPPermissionDeniedException extends Exception {

	private static final long serialVersionUID = -1515681635759400705L;

	public HTTPPermissionDeniedException(String message) {
		super(message);
	}

	public HTTPPermissionDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

}
