package FileServer;


import java.util.Date;

/**
 * This interface is designed to allow HTTP requests interpreted by the Request
 * Handler to be carried out.
 * <p>
 * Each method returns an array of <code>byte</code>s. If we used
 * <code>String</code>s instead, our server would be restricted to serving ASCII
 * files only (or at least whatever charset is supported by Java
 * <code>String</code> Objects). By using an array of <code>byte</code>s, we can
 * return any arbitrary string of zeros and ones, which could perhaps represent
 * a JPEG image or compiled executable.
 * 
 * @author G52APR
 * @since 2012/09/03
 * @version 1
 */

public interface IServe {
	/**
	 * 
	 * @param requestURI
	 *            the Request-URI from the associated GET request
	 * @return an array of <code>byte</code>s containing the requested data
	 * @throws HTTPFileNotFoundException
	 *             if the file specified by the Request-URI does not exist
	 * @throws HTTPRuntimeException
	 *             if the file specified by the Request-URI is executable but
	 *             did not execute correctly
	 * @throws HTTPPermissionDeniedException
	 *             if the file specified by the Request-URI is not permissioned
	 *             correctly for read or execution access (as appropriate)
	 */
	public byte[] httpGet(String requestURI) throws HTTPFileNotFoundException, HTTPRuntimeException,
			HTTPPermissionDeniedException;

	/**
	 * 
	 * @param requestURI
	 *            the Request-URI from the associated conditional GET request
	 * @param ifModifiedSince
	 *            a Date Object containing the date from the If-Modified-Since
	 *            header field
	 * @return an array of <code>byte</code>s containing the requested data
	 * @throws HTTPFileNotFoundException
	 *             if the file specified by the Request-URI does not exist
	 * @throws HTTPRuntimeException
	 *             if the file specified by the Request-URI is executable but
	 *             did not execute correctly
	 * @throws HTTPPermissionDeniedException
	 *             if the file specified by the Request-URI is not permissioned
	 *             correctly for read or execution access (as appropriate)
	 */
	public byte[] httpGETconditional(String requestURI, Date ifModifiedSince) throws HTTPFileNotFoundException,
			HTTPRuntimeException, HTTPPermissionDeniedException;

	/**
	 * 
	 * @param requestURI
	 *            the Request-URI from the associated HEAD request
	 * @return an array of <code>byte</code>s containing the requested data
	 * @throws HTTPFileNotFoundException
	 *             if the file specified by the Request-URI does not exist
	 * @throws HTTPRuntimeException
	 *             if the file specified by the Request-URI is executable but
	 *             did not execute correctly
	 * @throws HTTPPermissionDeniedException
	 *             if the file specified by the Request-URI is not permissioned
	 *             correctly for read or execution access (as appropriate)
	 */
	public byte[] httpHEAD(String requestURI) throws HTTPFileNotFoundException, HTTPRuntimeException,
			HTTPPermissionDeniedException;

	/**
	 * 
	 * @param requestURI
	 *            the Request-URI from the associated POST request
	 * @param postData
	 *            an array of <code>byte</code>s containing the POST data from
	 *            the POST request
	 * @return an array of <code>byte</code>s containing the requested data
	 * @throws HTTPFileNotFoundException
	 *             if the file specified by the Request-URI does not exist
	 * @throws HTTPRuntimeException
	 *             if the file specified by the Request-URI is executable but
	 *             did not execute correctly
	 * @throws HTTPPermissionDeniedException
	 *             if the file specified by the Request-URI is not permissioned
	 *             correctly for read or execution access (as appropriate)
	 */
	public byte[] httpPOST(String requestURI, byte[] postData) throws HTTPFileNotFoundException, HTTPRuntimeException,
			HTTPPermissionDeniedException;

}
