package dpss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
//import java.util.stream.Collectors;

public class CoreGameServer extends UnicastRemoteObject implements GameServerRMI {

	private static final long serialVersionUID = -3393990750400755075L;
	
	private HashMap<Character,ArrayList<Player>> playerHash = new HashMap<>();

	private String gameServerLocation;

	protected CoreGameServer(String location) throws RemoteException {
		super();
		this.gameServerLocation = location; 
		createPlayerAccount("Admin","Admin","Admin","Admin", getRegionDefaultIP(), 0);
	}
	
	private String getRegionDefaultIP() {
		switch(this.gameServerLocation) {
			case "NA":
				return "132.168.2.22";
			case "EU":
				return "93.168.2.22";
			case "AS":
				return "182.168.2.22";
		}
		return null;
	}

	private void serverLog(String logStatement, String ipAddress) {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		 LocalDateTime tStamp = LocalDateTime.now(); 
		 String writeString = String.format("[%s] Response to %s -- %s", dtf.format(tStamp), ipAddress, logStatement);
		 try{
			File file = new File(String.format("server_logs/%s-server.log", this.gameServerLocation));
			file.getParentFile().mkdirs();
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter logger = new BufferedWriter(fw);
			logger.write(writeString);
			logger.newLine();
			logger.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized String createPlayerAccount(String fName, String lName, String uName, String password, String ipAddress, int age) {
		serverLog("Initiating CREATEACCOUNT for player", ipAddress);
		
		Character uNameFirstChar = uName.charAt(0);
		String retString = "An Error was encountered!";
		
		if(!this.playerHash.containsKey(uNameFirstChar)) {
			this.playerHash.put(uNameFirstChar, new ArrayList<Player>());
		}
		
		try {
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
			
			serverLog(retString, ipAddress);
		} catch(BadUserNameException | BadPasswordException e) {
			retString = e.getMessage();
			serverLog(retString, ipAddress);
		}
		return retString; 
	}
	
	public synchronized String playerSignIn(String uName, String password, String ipAddress) {
		serverLog("Initiating SIGNIN for player", ipAddress);
		Character uNameFirstChar = uName.charAt(0);
		
		if(!this.playerHash.containsKey(uNameFirstChar)) {
			String errExist = String.format("Player with username '%s' does not exist", uName);
			serverLog(errExist, ipAddress);
			return errExist;
		}
		
		Optional<Player> playerToSignIn = this.playerHash.get(uNameFirstChar).stream().filter(player -> {
			return player.getuName().equals(uName) && player.getPassword().equals(password);
		}).findAny();
		
		if(playerToSignIn.isPresent()) {
			if(playerToSignIn.get().getStatus()) {
				String errSignedIn = String.format("Player '%s' is already signed in", uName); 
				serverLog(errSignedIn, ipAddress);
				return errSignedIn;
			} else {
				playerToSignIn.get().setStatus(true);
			}
			String success = String.format("Successfully signed in player with username -- '%s'",uName);
			serverLog(success, ipAddress);
			return success;
		}
		String errExist = String.format("Player with username '%s' and that password combination does not exist", uName);
		serverLog(errExist, ipAddress);
		return errExist;
	}
	
	public synchronized String playerSignOut(String uName, String ipAddress) {
		serverLog("Initiating SIGNOUT for player", ipAddress);
		Character uNameFirstChar = uName.charAt(0);
		
		if(!this.playerHash.containsKey(uNameFirstChar)) {
			String errExist = String.format("Player with username '%s' does not exist", uName);
			serverLog(errExist, ipAddress);
			return errExist;
		}
		
		Optional<Player> playerToSignOut = this.playerHash.get(uNameFirstChar).stream().filter(player -> {
			return player.getuName().equals(uName);
		}).findAny();
		
		if(playerToSignOut.isPresent()) {
			if(!playerToSignOut.get().getStatus()) {
				String errSignedOut = String.format("Player '%s' is already signed out", uName);
				serverLog(errSignedOut, ipAddress);
				return errSignedOut;
			} else {
				playerToSignOut.get().setStatus(false);
			}
			String success = String.format("Successfully signed out player with username -- '%s'",uName);
			serverLog(success, ipAddress);
			return success;
		}
		
		String errExist = String.format("Player with username '%s' and that password combination does not exist", uName);
		serverLog(errExist, ipAddress);
		return errExist;
	}

}
