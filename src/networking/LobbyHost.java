package networking;

import gamePieces.Race;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;

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
				Logger.loudLog(new Exception("LobbyGreeter thread has died!")); //should do something more than log - maybe spin new one back up?
			if(!messenger.isAlive())
				Logger.loudLog(new Exception("LobbyMessenger thread has died!")); //should do something more than log - maybe spin new one back up?
			synchronized(listLock){
				for(LobbyClient client: clientList){
					if(client.in.hasNextLine())
						processMessage(client.in.nextLine(), client);
				}
			}
		}
		greeter.interrupt();
		messenger.interrupt();
	}
	
	private void processMessage(String nextLine, LobbyClient sender) {
		String[] args = nextLine.split(":");
		String message = args[0];
		if(message.equals(LobbyMessages.CHAT)){
			//print message in lobby	
		}else if(message.equals(LobbyMessages.CHANGE_COLOR)){
			String[] ints = args[2].split(",");
			Color temp = new Color(Integer.parseInt(ints[0]), Integer.parseInt(ints[1]), Integer.parseInt(ints[2]), Integer.parseInt(ints[3]));
			lobby.getPlayerPanel(Integer.parseInt(args[1])).setColor(temp);
		}else if(message.equals(LobbyMessages.CHANGE_RACE)){
			Race r = Race.getRace(Integer.parseInt(args[2]));
			lobby.getPlayerPanel(Integer.parseInt(args[1])).getPlayer().setRace(r);
		}else if(message.equals(LobbyMessages.CHANGE_NAME)){
			lobby.getPlayerPanel(Integer.parseInt(args[1])).getPlayer().setName(args[2]);			
		}else if(message.equals(LobbyMessages.READY)){
			if(Boolean.parseBoolean(args[1]))
				lobby.getPlayerPanel(Integer.parseInt(args[1])).setReady(true);
			else
				lobby.getPlayerPanel(Integer.parseInt(args[1])).setReady(false);
		}else if(message.equals(LobbyMessages.QUIT_LOBBY)){
			lobby.getPlayerPanel(Integer.parseInt(args[1])).clear();
			synchronized(listLock){
				clientList.remove(sender);
			}
		}else return; //message does not match any known messages - don't forward it
		
		sendMessage(nextLine, sender);
	}
	
	public synchronized void sendMessage(String s, LobbyClient sender){
		//send the message to all connected clients (including host?)
	}

	public synchronized void startGame(){
		//clear outbound messages
		//tell all clients to start game
		//spin up game threads
	}
	
	public synchronized void closeLobby(){
		//clear outbound messages
		//tell all clients to exit lobby
		//spin down lobby threads
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
					temp = new LobbyClient(server.accept()); //accept new socket connection
					synchronized(listLock){					 //lock on the clientList lock
							List<Integer> open = lobby.openPlayerSlots();
							if(!clientList.contains(temp) && !open.isEmpty()){ //if the player hasn't connected already and we have an empty slot
								int pos =open.get(0);
								clientList.add(temp);
								temp.out.println(LobbyMessages.LOBBY_ACCEPT+":"+pos); //tell client of acceptance, give client position in lobby
								lobby.getPlayerPanel(pos).getPlayer().setName("Player " + pos);
							}else 
								temp.out.println(LobbyMessages.LOBBY_FULL); //tell client the lobby is full
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
				while(messages.size() > 0){ //while there are messages in the list
					synchronized(messageLock){
						message = messages.remove(); //lock the list, then pull the front off
					}
					synchronized(listLock){
						for(LobbyClient client: clientList){	//send the message to every client
							client.out.println(message);
						}
					}
				}
			}
		}
	}
	
}
