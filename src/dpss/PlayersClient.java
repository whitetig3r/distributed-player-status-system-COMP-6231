package dpss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class PlayersClient {

	private static Scanner sc = new Scanner(System.in);
	private static String serverToConnect;
	private static GameServerRMI serverStub;
	private static ServerDiscovererRMI discoveryStub;
	
	private static final String ERR_BAD_IP = "The Server IP Address is invalid!";
	
	

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
	
	private static void log(String logStatement, String uName, String ipAddress) {
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
	
	private static void realizeCreatePlayerAccount(String fName, String lName, String uName, String password, int age, String ipAddress) throws MalformedURLException, RemoteException, NotBoundException {
		discoveryStub = (ServerDiscovererRMI) Naming.lookup("rmi://127.0.0.1:1098/Discover");
		serverToConnect = discoveryStub.getRegionServer(ipAddress);
		
		String logStatement = "Requesting Create Player Account Action on Region Server -- " + serverToConnect;
		System.out.println(logStatement);
		
		if(serverToConnect != null) {
			serverStub = (GameServerRMI) Naming.lookup("rmi://127.0.0.1:1098" + serverToConnect);
			String retStatement = serverStub.createPlayerAccount(fName, lName, uName, password, ipAddress, age);
			System.out.println(retStatement);
			log(logStatement, uName, ipAddress);
			log(retStatement, uName, ipAddress);
		} else {
			System.out.println(ERR_BAD_IP);
			log(ERR_BAD_IP, uName, "BAD_IP_ADDR");
		}
	}
	
	private static void realizePlayerSignIn(String uName, String password, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		discoveryStub = (ServerDiscovererRMI) Naming.lookup("rmi://127.0.0.1:1098/Discover");
		serverToConnect = discoveryStub.getRegionServer(ipAddress);
		
		String logStatement = "Requesting Sign-In Action on Region Server -- " + serverToConnect;
		System.out.println(logStatement);
		
		if(serverToConnect != null) {
			serverStub = (GameServerRMI) Naming.lookup("rmi://127.0.0.1:1098" + serverToConnect);
			String retStatement = serverStub.playerSignIn(uName, password, ipAddress);
			System.out.println(retStatement);
			log(logStatement, uName, ipAddress);
			log(retStatement, uName, ipAddress);
		} else {
			System.out.println(ERR_BAD_IP);
			log(ERR_BAD_IP, uName, "BAD_IP_ADDR");
		}
	}

	private static void realizePlayerSignOut(String uName, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		discoveryStub = (ServerDiscovererRMI) Naming.lookup("rmi://127.0.0.1:1098/Discover");
		serverToConnect = discoveryStub.getRegionServer(ipAddress);
		
		String logStatement = "Requesting Sign-Out Action on Region Server -- " + serverToConnect;
		System.out.println(logStatement);
		
		if(serverToConnect != null) {
			serverStub = (GameServerRMI) Naming.lookup("rmi://127.0.0.1:1098" + serverToConnect);
			String retStatement = serverStub.playerSignOut(uName, ipAddress);
			System.out.println(retStatement);
			log(logStatement, uName, ipAddress);
			log(retStatement, uName, ipAddress);
		} else {
			System.out.println(ERR_BAD_IP);
			log(ERR_BAD_IP, uName, "BAD_IP_ADDR");
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
		ipAddress = sc.nextLine();
		
		try {
			realizeCreatePlayerAccount(fName, lName, uName, password, age, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
			log(e.getMessage(), uName, ipAddress);
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
		ipAddress = sc.nextLine();
		
		try {
			realizePlayerSignIn(uName, password, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
			log(e.getMessage(), uName, ipAddress);
		}

	}
	
	private static void playerSignOut() {
		String uName;
		String ipAddress;
		System.out.println("Enter User Name:");
		uName = sc.nextLine();
		System.out.println("Enter IP Address:");
		ipAddress = sc.nextLine();
		
		try {
			realizePlayerSignOut(uName, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log(e.getMessage(), uName, ipAddress);
		}

	}

}