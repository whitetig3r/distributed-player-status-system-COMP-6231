package clients;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.ConnectException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import servers.GameServerRMI;

public class PlayersClient extends CoreClient {

	private static Scanner sc = new Scanner(System.in);
	private static GameServerRMI serverStub; 
	

	public static void main(String[] args) {
		final String MENU_STRING = "\n-- Player Client CLI --\n"
				+ "Pick an option ...\n"
				+ "1. Create a Player account\n"
				+ "2. Sign a Player in\n"
				+ "3. Sign a Player out\n"
				+ "4. Exit the CLI\n"
				+ "--------------------------\n";
		try {
			// TODO Build an interactive menu driven UI
			System.out.println("NOTE -- Player Logs available at " + System.getProperty("user.dir") + "/player_logs/");
			while(true) {
				System.out.println(MENU_STRING);
				switch(sc.nextLine()) {
					case "1": {
						createPlayerAccount();
						break;
					}
					case "2": {
						playerSignIn();
						break;
					}
					case "3": {
						playerSignOut();
						break;
					}
					case "4": {
						System.out.println("Goodbye!");
						System.exit(0);
					}
					default: {
						System.out.println("Invalid Option selected!");
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void createPlayerAccount() {
		String fName;
		String lName;
		String uName;
		String password;
		String ipAddress;
		int age;
		
		setLoggingContext("UNRESOLVED_PLAYER", "UnresolvedIP");
		fName = getSafeStringInput("Enter First Name:");
		lName = getSafeStringInput("Enter Last Name:");
		uName = getSafeStringInput("Enter User Name:");
		password = getSafeStringInput("Enter Password:");
		age = getSafeIntInput("Enter Age:");
		
		System.out.println("Enter IP Address:");
		ipAddress = getIpAddressInput();
		
		try {
			realizeCreatePlayerAccount(fName, lName, uName, password, age, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			handleServerDown(uName, ipAddress, e);
		}
	}
		
	private static void playerSignIn() {
		String uName;
		String password;
		String ipAddress;
		
		setLoggingContext("UNRESOLVED_PLAYER", "UnresolvedIP");
		uName = getSafeStringInput("Enter User Name:");
		password = getSafeStringInput("Enter Password:");
		System.out.println("Enter IP Address:");
		ipAddress = getIpAddressInput();
		try {
			realizePlayerSignIn(uName, password, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			handleServerDown(uName, ipAddress, e);
		}

	}
	
	private static void playerSignOut() {
		String uName;
		String ipAddress;
		
		setLoggingContext("UNRESOLVED_PLAYER", "UnresolvedIP");
		uName = getSafeStringInput("Enter User Name:");
		System.out.println("Enter IP Address:");
		ipAddress = getIpAddressInput();
		try {
			realizePlayerSignOut(uName, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			handleServerDown(uName, ipAddress, e);
		}

	}
	
	private static void realizeCreatePlayerAccount(String fName, String lName, String uName, String password, int age, String ipAddress) throws MalformedURLException, RemoteException, NotBoundException {
		int registryPort = getRegionServer(ipAddress);
		
		serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",registryPort));
		String retStatement = serverStub.createPlayerAccount(fName, lName, uName, password, ipAddress, age);
		System.out.println(retStatement);
		playerLog(retStatement, uName, ipAddress);
	}
	
	private static void realizePlayerSignIn(String uName, String password, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		int registryPort = getRegionServer(ipAddress);
		
		serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",registryPort));
		String retStatement = serverStub.playerSignIn(uName, password, ipAddress);
		System.out.println(retStatement);
		playerLog(retStatement, uName, ipAddress);
	}

	private static void realizePlayerSignOut(String uName, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		int registryPort = getRegionServer(ipAddress);
		
		serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",registryPort));
		String retStatement = serverStub.playerSignOut(uName, ipAddress);
		System.out.println(retStatement);
		playerLog(retStatement, uName, ipAddress);
	}
	
	private static void handleServerDown(String uName, String ipAddress, Exception e) {
		String err = e.getMessage();
		if(e instanceof ConnectException) {
			err = "ERROR: Region server is not active";
			System.out.println(err);
		}
		playerLog(err, uName, ipAddress);
	}

}