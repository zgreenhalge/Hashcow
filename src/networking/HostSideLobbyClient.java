package networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import utils.Logger;

public class HostSideLobbyClient {

	public PrintWriter out;
	public Scanner in;
	private Socket socket;
	
	public HostSideLobbyClient(Socket sock) throws IOException{
		socket = sock;
		out = new PrintWriter(sock.getOutputStream());
		in = new Scanner(sock.getInputStream());
	}
	
	public void close(){
		try {
			socket.close();
		} catch (IOException e) {
			Logger.loudLog(e);
		}
		in.close();
		out.flush();
		out.close();
	}
	
}
