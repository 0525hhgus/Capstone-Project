import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// 승차 요청 서버
public class ServerTest2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		
		serverSocket = new ServerSocket(1234);
		
		try {
			clientSocket = serverSocket.accept();
			System.out.println("Client connect");
			
			out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			
			while(true) {
				String inputLine = null;
				inputLine = in.readLine();
				
				System.out.println("클라이언트로부터 받은 승차 요청 버스 ID : " + inputLine);
				out.println(inputLine);
				
				if(inputLine.equals("null"))
					break;
				
			}
			
			out.close();
			in.close();
			
			clientSocket.close();
			serverSocket.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

}
