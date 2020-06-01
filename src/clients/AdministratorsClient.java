package clients;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import exceptions.UnknownServerRegionException;
import server.GameServerRMI;

public class AdministratorsClient {
	
	private static Scanner sc = new Scanner(System.in);
	private static GameServerRMI serverStub;
	private static String serverToConnect;
	

	public static void main(String[] args) {
		// TODO Build an interactive menu driven UI
		System.out.println("NOTE -- Admin Logs available at " + System.getProperty("user.dir") + "/admin_logs");
		final String MENU_STRING = "\n-- Admin Client CLI --\n"
				+ "Pick an option ...\n"
				+ "1. Sign in with admin privileges\n"
				+ "2. Sign out admin\n"
				+ "3. Get status of all players playing the game\n"
				+ "4. Exit the CLI\n"
				+ "--------------------------\n";
		while(true) {
			System.out.println(MENU_STRING);
			switch(sc.nextLine()) {
				case "1": {
					adminSignIn();
					break;
				}
				case "2": {
					adminSignOut();
					break;
				}
				case "3": {
					adminGetPlayerStatus();
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
	}
	
	private static void adminSignIn() {
		String uName;
		String password;
		String ipAddress;
		System.out.println("Enter User Name:");
		uName = sc.nextLine();
		System.out.println("Enter Password:");
		password = sc.nextLine();
		System.out.println("Enter IP Address:");
		ipAddress = ClientUtilities.getIpAddressInput();
		
		try {
			realizeAdminSignIn(uName, password, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			handleServerDown(uName, ipAddress, e);
		}

	}
	
	private static void adminSignOut() {
		String uName;
		String ipAddress;
		System.out.println("Enter User Name:");
		uName = sc.nextLine();
		System.out.println("Enter IP Address:");
		ipAddress = ClientUtilities.getIpAddressInput();
		
		try {
			realizeAdminSignOut(uName, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			handleServerDown(uName, ipAddress, e);
		}

	}
	
	private static void adminGetPlayerStatus() {
		String uName;
		String password;
		String ipAddress;
		System.out.println("Enter User Name:");
		uName = sc.nextLine();
		System.out.println("Enter Password:");
		password = sc.nextLine();
		System.out.println("Enter IP Address:");
		ipAddress = ClientUtilities.getIpAddressInput();
		
		try {
			realizeAdminGetPlayerStatus(uName, password, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException | UnknownServerRegionException e) {
			handleServerDown(uName, ipAddress, e);
		}

	}

	private static void realizeAdminSignIn(String uName, String password, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		int registryPort = ClientUtilities.getRegionServer(ipAddress);
		serverToConnect = ClientUtilities.getServerName(registryPort);
		
		serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",registryPort));
		String retStatement = serverStub.adminSignIn(uName, password, ipAddress);
		System.out.println(retStatement);
		log(retStatement, uName, serverToConnect);
	}
	
	private static void realizeAdminSignOut(String uName, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		int registryPort = ClientUtilities.getRegionServer(ipAddress);
		serverToConnect = ClientUtilities.getServerName(registryPort);
		
		serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",registryPort));
		String retStatement = serverStub.adminSignOut(uName, ipAddress);
		System.out.println(retStatement);
		log(retStatement, uName, serverToConnect);
	}
	
	private static void realizeAdminGetPlayerStatus(String uName, String password, String ipAddress) throws MalformedURLException, RemoteException, NotBoundException, UnknownServerRegionException{
		int registryPort = ClientUtilities.getRegionServer(ipAddress);
		serverToConnect = ClientUtilities.getServerName(registryPort);

		serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",registryPort));
		String retStatement = serverStub.getPlayerStatus(uName, password, ipAddress);
		System.out.println(retStatement);
		log(retStatement, uName, serverToConnect);

	}
	
	private static synchronized void log(String logStatement, String uName, String serverToConnect) {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		 LocalDateTime tStamp = LocalDateTime.now(); 
		 String writeString = String.format("[%s] %s @ (Admin-%s) -- %s", dtf.format(tStamp), uName, serverToConnect.substring(1), logStatement);
		 try{
			File file = new File(String.format("admin_logs/%s-admin.log", serverToConnect.substring(1)));
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
	
	private static void handleServerDown(String uName, String ipAddress, Exception e) {
		String err = e.getMessage();
		if(e instanceof ConnectException) {
			err = "ERROR: Region server is not active";
			System.out.println(err);
		}
		log(err, uName, ipAddress);
	}
	

}
