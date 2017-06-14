import java.io.* ;
import java.nio.file.*;
import java.net.* ;
import java.util.* ;

public class WebClient{
	private static String hostName = "localhost";
	private static int portNumber = 6789;
	public static void main(String[] args)  throws Exception{
		try (
			Socket client = new Socket(hostName, portNumber);
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			)
		{
			out.write("GET ./index.htm HTTP/1.1\r\n");
			System.out.println("send the GET");
			out.write("Host: localhost\r\n");
			out.write("Connection: close\r\n");
			out.write("User-agent: caiovico/2.7\r\n");
			out.write("Accept-language: pt\r\n");
		}
	}
}