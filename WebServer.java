import java.io.* ;
import java.nio.file.*;
import java.net.* ;
import java.util.* ;
import java.util.stream.*;
import java.util.concurrent.*;
final class HttpRequest implements Runnable{
	private Socket client;
	private  void sendBytes(FileInputStream fis, OutputStream os) throws Exception{
		 // Construct a 1K buffer to hold bytes on their way to the socket.
		 byte[] buffer = new byte[1024];
		 int bytes = 0;
		 // Copy requested file into the socket's output stream.
		 while((bytes = fis.read(buffer)) != -1 ) {
		 	System.out.println("Trying to send file to client...");
		 	os.write(buffer, 0, bytes);
 		}
	}

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
		System.out.println("\nTest A.\n");
		out.writeBytes("400 Bad Request\r\n\r\n");
		FileInputStream fis = null;
		boolean fileExists = true;
		try{
			fis = new FileInputStream(filename);
		}catch (FileNotFoundException e){
			fileExists = false;
			System.out.println("File Not Found.");
		}
		if (fileExists){
			String stateLine = null;
			String headLine = "Content-type: text/html \r\n";
			if (true){
				System.out.println("\nTest B.\n");
				stateLine = "200 OK \r\n";
				out.writeBytes(stateLine);
				out.writeBytes(headLine);
				String testToSend = "<html><body><h1>TEST OK!</h1></body></html>";
				try{
					sendBytes(fis,out);
				}catch(Exception e){
					e.printStackTrace();
				}	
		}
		else{

			}
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

