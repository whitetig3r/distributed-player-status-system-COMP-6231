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

import server.GameServerRMI;

public class PlayersClient {

	private static Scanner sc = new Scanner(System.in);
	private static GameServerRMI serverStub;
	

	public static void main(String[] args) {
		try {
			System.out.println("NOTE -- Player Logs available at " + System.getProperty("user.dir") + "/player_logs");
			createPlayerAccount();
			playerSignIn();
			playerSignOut();
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
		
		System.out.println("Enter First Name:");
		fName = sc.nextLine();
		System.out.println("Enter Last Name:");
		lName = sc.nextLine();
		System.out.println("Enter User Name:");
		uName = sc.nextLine();
		System.out.println("Enter Password:");
		password = sc.nextLine();
		System.out.println("Enter Age:");
		age = sc.nextInt();
		sc.nextLine();
		System.out.println("Enter IP Address:");
		ipAddress = ClientUtilities.getIpAddressInput();
		try {
			realizeCreatePlayerAccount(fName, lName, uName, password, age, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			String err = e.getMessage();
			if(e instanceof ConnectException) {
				err = "ERROR: Region server is not active";
				System.out.println(err);
			}
			log(err, uName, ipAddress);
		}
	}
		
	private static void playerSignIn() {
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
			realizePlayerSignIn(uName, password, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			String err = e.getMessage();
			if(e instanceof ConnectException) {
				err = "ERROR: Region server is not active";
				System.out.println(err);
			}
			log(err, uName, ipAddress);
		}

	}
	
	private static void playerSignOut() {
		String uName;
		String ipAddress;
		
		System.out.println("Enter User Name:");
		uName = sc.nextLine();
		System.out.println("Enter IP Address:");
		ipAddress = ClientUtilities.getIpAddressInput();
		try {
			realizePlayerSignOut(uName, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			String err = e.getMessage();
			if(e instanceof ConnectException) {
				err = "ERROR: Region server is not active";
				System.out.println(err);
			}
			log(err, uName, ipAddress);
		}

	}
	
	private static void realizeCreatePlayerAccount(String fName, String lName, String uName, String password, int age, String ipAddress) throws MalformedURLException, RemoteException, NotBoundException {
		int registryPort = ClientUtilities.getRegionServer(ipAddress);
		
		serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",registryPort));
		String retStatement = serverStub.createPlayerAccount(fName, lName, uName, password, ipAddress, age);
		System.out.println(retStatement);
		log(retStatement, uName, ipAddress);
	}
	
	private static void realizePlayerSignIn(String uName, String password, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		int registryPort = ClientUtilities.getRegionServer(ipAddress);
		
		serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",registryPort));
		String retStatement = serverStub.playerSignIn(uName, password, ipAddress);
		System.out.println(retStatement);
		log(retStatement, uName, ipAddress);
	}

	private static void realizePlayerSignOut(String uName, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		int registryPort = ClientUtilities.getRegionServer(ipAddress);
		
		serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",registryPort));
		String retStatement = serverStub.playerSignOut(uName, ipAddress);
		System.out.println(retStatement);
		log(retStatement, uName, ipAddress);
	}
	
	private static synchronized void log(String logStatement, String uName, String ipAddress) {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		 LocalDateTime tStamp = LocalDateTime.now(); 
		 String writeString = String.format("[%s] %s @ (%s) -- %s", dtf.format(tStamp), uName, ipAddress, logStatement);
		 try{
			File file = new File(String.format("player_logs/%s.log", uName));
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

}