import java.io.* ;
import java.net.* ;
import java.util.* ;
import java.util.concurrent.*;
final class HttpRequest implements Runnable{
	private Socket client;
	public HttpRequest(Socket s){
		client = s;
	}
	public void processRequest() throws Exception{
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		Writer out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));


		in.lines().forEach(System.out::println);
	}
	public void run(){
		try{
			processRequest();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}

public class WebServer{
	static int port = 80;
	public static void main(String args[]) throws Exception{
		if (args.length!=0){
			port = Integer.parseInt(args[0]);
		}
		ServerSocket server =  new ServerSocket(port);
		ExecutorService threads = Executors.newCachedThreadPool();
		while(true){
			Socket client = server.accept();
			threads.submit(new HttpRequest(client));
		}
	}
}	

