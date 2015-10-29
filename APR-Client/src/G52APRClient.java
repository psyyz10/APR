import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


/* You will need to use this class to build your G52APRClient.
 * It needs to implement the interface IG52APRClient and so it
 * will need to have the methods for httpGet, httpHead and httpPost
 */ 
public class G52APRClient implements IG52APRClient {
	private DefaultHttpClient httpClient;
	
	/* Construct a G52APRClient */
	public G52APRClient(){
		httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				"http.protocol.version", HttpVersion.HTTP_1_0);
	}
	
	/* GET method */
	public String httpGet(String url){
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = null;	
		
		try {
			response = httpClient.execute(httpGet);
			
			// Check status code whether equals HttpStatus.SC_OK
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				return response.getStatusLine().toString();
			
			// Get and return entity as a string
			HttpEntity entity = response.getEntity();
			if (entity != null){
				return EntityUtils.toString(entity);
			}
			else{
				return null;
			}
		} catch (ClientProtocolException e) {
			return e.toString();
		} catch (IOException e) {
			return e.toString();
		}finally { 
			httpGet.releaseConnection(); 
		}
	}
	
	/*HEAD method */
	public String httpHead(String url) {
        HttpHead httpHead = new HttpHead(url);
        HttpResponse response = null;
        
		try {
			response = httpClient.execute(httpHead);
			
			// Check status code whether equals HttpStatus.SC_OK
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				return response.getStatusLine().toString();
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally { 
			httpHead.releaseConnection(); 
		}
		
		//Get and return header
        HeaderIterator it = response.headerIterator();
        String headers = new String();
        while (it.hasNext()) {
            headers += it.next() + "\n";
        }
		return headers;
	}
	
	/* Post method */
	public String httpPost(String url, String body){
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		
		try {
			//Set body as StringEntity
			post.setEntity(new StringEntity(body));
			response = httpClient.execute(post);
			
			// Check status code whether equals HttpStatus.SC_OK
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				return response.getStatusLine().toString();
			
			// Get and return entity as a string
			HttpEntity entity = response.getEntity();
			if (entity != null){
				return EntityUtils.toString(entity);
			}
			else{
				return null;
			}
		} catch (ClientProtocolException e) {
			return e.toString();
		} catch (IOException e) {
			return e.toString();
		} finally { 
			post.releaseConnection(); 
		}
	}
}
