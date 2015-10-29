/**
 *
 * @author G52APR
 */
public interface IG52APRClient {

    /**
     * This method gets the body of the content from a URL and returns it as a string
     * hint: You should check the status code of your response by using the responses getStatusLine().getStatusCode() method.
     * any status other than HttpStatus.SC_OK should be handled by returning the status line
     *
     * @param url The url of the resource to get
     * @return the body of the response
     */
    public abstract String httpGet(String url);

    /** 
     * This method gets the headers only and returns it as a string in the following format:
     * <Name> : <Value>
     * For example:
     * Content-Type: text/html
     * each header should be on it's own line (Separated by \n).
     * hint: You should get the status code of your response by using the responses getStatusLine().getStatusCode() method.
     * any status other than HttpStatus.SC_OK should be handled by returning the status line.
     *
     * @param url The url of the resource.
     * @return The headers of the response formatted as a string.
     */
    public abstract String httpHead(String url);

    /**
     * This method sends data to the server for using HttpPost, and returns the body of the response.
     * hint: You should get the status code of your response by using the responses getStatusLine().getStatusCode() method.
     * any status other than HttpStatus.SC_OK should be handled by returning the status line
     *
     * @param url the url of the resource.
     * @param body the body of the request to be sent to the server.
     * @return The body of the response.
     */
    public abstract String httpPost(String url, String body);



}