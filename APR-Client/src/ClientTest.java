
public class ClientTest {
	private G52APRClient client;
	
	public ClientTest(G52APRClient client){
		this.client = client;
	}
	
	public void runTests(){
		System.out.println(client.httpGet("http://cs.nott.ac.uk/~cah/") + "\n");
		System.out.println(client.httpHead("http://cs.nott.ac.uk/~cah/") + "\n");
		System.out.println(client.httpPost("http://posttestserver.com/post.php", "APR") + "\n");
		System.out.println(client.httpGet("http://localhost:4444"));
		System.out.println(client.httpHead("http://localhost:4444"));
		System.out.println(client.httpPost("http://localhost:4444", "APR"));
	}
	
	public static void main(String[] args){
		ClientTest ct = new ClientTest(new G52APRClient());
		ct.runTests();
	}
}
