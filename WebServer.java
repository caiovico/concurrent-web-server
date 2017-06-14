import java.io.* ;
import java.nio.file.*;
import java.net.* ;
import java.util.* ;
import java.util.stream.*;
import java.util.concurrent.*;
final class HttpRequest implements Runnable{
	private Socket client;
	public HttpRequest(Socket s){
		client = s;
	}
	public void processRequest() throws Exception{
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		String filename = null;
		String line = in.readLine();
		if (line != null){
			filename = "./index.htm";
			String[] request = line.split(" ");
			if (!request[1] .equals("/") ){
				filename = "."+request[1];
			}	
		}
		System.out.format("File requested - > %s\n\n", filename);
		line = null;
		while ((line = in.readLine()) != null){
			System.out.println(line);
		}
		FileInputStream fis = null;
		boolean fileExists = true;
		try{
			fis = new FileInputStream(filename);
		}catch (FileNotFoundException e){
			fileExists = false;
			System.out.println("File Not Found.");
		}
		String stateLine = null;
		if (fileExists){
			String headLine = "Content-type: text/html \r\n";
			System.out.println("\nTest B.\n");
			stateLine = "200 OK \r\n";
			out.writeBytes(stateLine);
			out.writeBytes(headLine);
			String testToSend = "<html><body><h1>TEST OK!</h1></body></html>";
			out.writeBytes(testToSend);
		}
		else{
			stateLine = "400 Bad Request\r\n\r\n";
			out.writeBytes(stateLine);
		}
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
	static int port = 6789;
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

