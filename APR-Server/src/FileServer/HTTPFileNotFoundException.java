package FileServer;

/**
 * This class provides a custom Exception that can be thrown if the file
 * specified by the Request-URI does not exist.
 * <p>
 * Example usage: if (e.g.) <code>/hello.html</code> does not exist:
 * <p>
 * <code>throw new HTTPFileNotFoundException("File /hello.html not found");</code>
 * <p>
 * You may include additional information if you wish by passing a
 * <code>Throwable</code> Object that caused the problem (in this case perhaps a
 * <code>FileNotFoundException</code>)
 * <p>
 * This might suggest a 404 Not Found error.
 * 
 * @author G52APR
 * @since 2012/09/03
 * @version 1
 */
public class HTTPFileNotFoundException extends Exception {

	private static final long serialVersionUID = -7227662939973493092L;

	public HTTPFileNotFoundException(String message) {
		super(message);
	}

	public HTTPFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
