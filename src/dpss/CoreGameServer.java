package dpss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
//import java.util.stream.Collectors;

public class CoreGameServer extends UnicastRemoteObject implements GameServerRMI {

	private static final long serialVersionUID = -3393990750400755075L;
	
	private HashMap<Character,ArrayList<Player>> playerHash = new HashMap<>();

	protected CoreGameServer() throws RemoteException {
		super();
	}
	
	public synchronized String createPlayerAccount(String fName, String lName, String uName, String password, String ipAddress, int age) {
		Character uNameFirstChar = uName.charAt(0);
		uNameFirstChar = Character.toLowerCase(uNameFirstChar);
		String retString;
		
		if(!this.playerHash.containsKey(uNameFirstChar)) {
			this.playerHash.put(uNameFirstChar, new ArrayList<Player>());
		}
		
		Player playerToAdd = new Player(fName, lName, uName, password, ipAddress, age);
		
		Optional<Player> playerExists = this.playerHash.get(uNameFirstChar)
				.stream().filter(player -> player.getuName().equals(uName)).findAny();
		
		if(playerExists.isPresent()) {
			retString = "Player with that username already exists!";
		} else {
			this.playerHash.get(uNameFirstChar).add(playerToAdd);
			// retString = this.playerHash.get(uNameFirstChar).stream().map(Player::getfName).collect(Collectors.joining("\n"));
			retString = String.format("Successfully created account for player with username -- '%s'", uName);
		}
		
		return retString; 
	}
	
	public synchronized String playerSignIn(String uName, String password, String ipAddress) {
		Character uNameFirstChar = uName.charAt(0);
		uNameFirstChar = Character.toLowerCase(uNameFirstChar);
		
		if(!this.playerHash.containsKey(uNameFirstChar)) {
			return String.format("Player with username '%s' does not exist", uName);
		}
		
		Optional<Player> playerToSignIn = this.playerHash.get(uNameFirstChar).stream().filter(player -> {
			return player.getuName().equals(uName) && player.getPassword().equals(password);
		}).findAny();
		
		if(playerToSignIn.isPresent()) {
			if(playerToSignIn.get().getStatus()) {
				return String.format("Player '%s' is already signed in", uName);
			} else {
				playerToSignIn.get().setStatus(true);
			}
			return String.format("Successfully signed in player with username -- '%s'",uName);
		}
		
		return String.format("Player with username '%s' does not exist", uName);
	}
	
	public synchronized String playerSignOut(String uName, String ipAddress) {
		Character uNameFirstChar = uName.charAt(0);
		uNameFirstChar = Character.toLowerCase(uNameFirstChar);
		
		if(!this.playerHash.containsKey(uNameFirstChar)) {
			return String.format("Player with username '%s' does not exist", uName);
		}
		
		Optional<Player> playerToSignOut = this.playerHash.get(uNameFirstChar).stream().filter(player -> {
			return player.getuName().equals(uName);
		}).findAny();
		
		if(playerToSignOut.isPresent()) {
			if(!playerToSignOut.get().getStatus()) {
				return String.format("Player '%s' is already signed out", uName);
			} else {
				playerToSignOut.get().setStatus(false);
			}
			return String.format("Successfully signed out player with username -- '%s'",uName);
		}
		
		return String.format("Player with username '%s' does not exist", uName);
	}

}
