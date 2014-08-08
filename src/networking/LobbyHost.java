package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import theGame.GameLobbyState;
import utils.Logger;

public class LobbyHost extends Thread {

	private static final int PORT = 0; //automatically finds an open port - should find a static port eventually
	
	private static Object listLock = new Object();
	private static Object messageLock = new Object();
	
	private GameLobbyState lobby;
	private ArrayList<LobbyClient> clientList;
	private LinkedList<String> messages;
	//private ArrayList
	
	public LobbyHost(GameLobbyState lobby) throws IOException{
		this.lobby = lobby;
		clientList = new ArrayList<LobbyClient>();
		messages = new LinkedList<String>();
	}
	
	@Override
	public void run(){
		Thread greeter = new Thread(new LobbyGreeter());
		Thread messenger = new Thread(new LobbySender());
		greeter.start();
		messenger.start();
		while(true){
			if(Thread.interrupted()) //manual check for interrupt
				break;
			if(!greeter.isAlive())
				Logger.loudLog(new Exception("LobbyGreeter thread has died!"));
			if(!messenger.isAlive())
				Logger.loudLog(new Exception("LobbyMessenger thread has died!"));
			synchronized(listLock){
				for(LobbyClient client: clientList){
					if(client.in.hasNextLine())
						processMessage(client.in.nextLine());
				}
			}
		}
		greeter.interrupt();
		messenger.interrupt();
	}
	
	private void processMessage(String nextLine) {
		//read String
		//perform required Actions
		//send to outbound queue
	}

	public synchronized void startGame(){
		
	}
	
	public synchronized void closeLobby(){
		
	}

	/**
	 * A Runnable that will listen on the specified port and accept new players until the Lobby is full
	 *
	 */
	private class LobbyGreeter implements Runnable{
		
		@Override
		public void run() {
			try {
				ServerSocket server = new ServerSocket(PORT);
				LobbyClient temp;
				while(true){
					if(Thread.interrupted()){
						server.close();
						return;
					}
					temp = new LobbyClient(server.accept());
					synchronized(listLock){
							List<Integer> open = lobby.openPlayerSlots();
							if(!clientList.contains(temp) && !open.isEmpty()){
								clientList.add(temp);
								temp.out.println(LobbyMessages.LOBBY_ACCEPT+":"+open.get(0));
							}else 
								temp.out.println(LobbyMessages.LOBBY_FULL);
					}
				}
			} catch (IOException e) {
				Logger.loudLog(e);
				return; //must exit if there is an error establishing the server
			}
		}
		
	}

	/**
	 * A Runnable that will continually remove Strings from the message queue and send them to each LobbyClient
	 *
	 */
	private class LobbySender implements Runnable{

		@Override
		public void run() {
			String message;
			while(true){
				if(Thread.interrupted())
					return;
				while(messages.size() > 0){
					synchronized(messageLock){
						message = messages.remove();
					}
					synchronized(listLock){
						for(LobbyClient client: clientList){
							client.out.println(message);
						}
					}
				}
			}
		}
	}
}
