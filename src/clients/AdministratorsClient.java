package clients;

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

import exceptions.UnknownServerRegionException;
import server.GameServerRMI;
import server.ServerDiscovererRMI;

public class AdministratorsClient {
	
	private static final String ERR_BAD_IP = "The Server IP Address is invalid!";
	private static Scanner sc = new Scanner(System.in);
	private static ServerDiscovererRMI discoveryStub;
	private static String serverToConnect;
	private static GameServerRMI serverStub;
	

	public static void main(String[] args) {
		System.out.println("NOTE -- Admin Logs available at " + System.getProperty("user.dir") + "/admin_logs");
		adminSignIn();
		adminGetPlayerStatus();
		adminSignOut();
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
		ipAddress = sc.nextLine();
		
		try {
			realizeAdminSignIn(uName, password, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
			log(e.getMessage(), uName, ipAddress);
		}

	}
	
	private static void adminSignOut() {
		String uName;
		String ipAddress;
		System.out.println("Enter User Name:");
		uName = sc.nextLine();
		System.out.println("Enter IP Address:");
		ipAddress = sc.nextLine();
		
		try {
			realizeAdminSignOut(uName, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log(e.getMessage(), uName, ipAddress);
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
		ipAddress = sc.nextLine();
		
		try {
			realizeAdminGetPlayerStatus(uName, password, ipAddress);
		} catch (MalformedURLException | RemoteException | NotBoundException | UnknownServerRegionException e) {
			e.printStackTrace();
			log(e.getMessage(), uName, ipAddress);
		}

	}
	
	private static void realizeAdminSignIn(String uName, String password, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		discoveryStub = (ServerDiscovererRMI) Naming.lookup("rmi://127.0.0.1:1098/Discover");
		serverToConnect = discoveryStub.getRegionServer(ipAddress);
		
		String logStatement = "Requesting Sign-In Action on Region Server -- " + (serverToConnect == null ? "Unrecognized Geo-Region" : serverToConnect);
		System.out.println(logStatement);
		
		if(serverToConnect != null) {
			serverStub = (GameServerRMI) Naming.lookup("rmi://127.0.0.1:1098" + serverToConnect);
			String retStatement = serverStub.adminSignIn(uName, password, ipAddress);
			System.out.println(retStatement);
			log(logStatement, uName, serverToConnect);
			log(retStatement, uName, serverToConnect);
		} else {
			System.out.println(ERR_BAD_IP);
			log(ERR_BAD_IP, uName, "BAD_IP_ADDR");
		}
	}
	
	private static void realizeAdminSignOut(String uName, String ipAddress) throws RemoteException, MalformedURLException, NotBoundException {
		discoveryStub = (ServerDiscovererRMI) Naming.lookup("rmi://127.0.0.1:1098/Discover");
		serverToConnect = discoveryStub.getRegionServer(ipAddress);
		
		String logStatement = "Requesting Sign-Out Action on Region Server -- " + (serverToConnect == null ? "Unrecognized Geo-Region" : serverToConnect);
		System.out.println(logStatement);
		
		if(serverToConnect != null) {
			serverStub = (GameServerRMI) Naming.lookup("rmi://127.0.0.1:1098" + serverToConnect);
			String retStatement = serverStub.adminSignOut(uName, ipAddress);
			System.out.println(retStatement);
			log(logStatement, uName, serverToConnect);
			log(retStatement, uName, serverToConnect);
		} else {
			System.out.println(ERR_BAD_IP);
			log(ERR_BAD_IP, uName, "BAD_IP_ADDR");
		}
	}
	
	private static void realizeAdminGetPlayerStatus(String uName, String password, String ipAddress) throws MalformedURLException, RemoteException, NotBoundException, UnknownServerRegionException{
		discoveryStub = (ServerDiscovererRMI) Naming.lookup("rmi://127.0.0.1:1098/Discover");
		serverToConnect = discoveryStub.getRegionServer(ipAddress);
		
		String logStatement = "Requesting getPlayerStatus Action on Region Server -- " + (serverToConnect  == null ? "Unrecognized Geo-Region" : serverToConnect);
		System.out.println(logStatement);
		
		if(serverToConnect != null) {
			serverStub = (GameServerRMI) Naming.lookup("rmi://127.0.0.1:1098" + serverToConnect);
			String retStatement = serverStub.getPlayerStatus(uName, password, ipAddress);
			System.out.println(retStatement);
			log(logStatement, uName, serverToConnect);
			log(retStatement, uName, serverToConnect);
		} else {
			System.out.println(ERR_BAD_IP);
			log(ERR_BAD_IP, uName, "BAD_IP_ADDR");
		}
	}
	
	
	private static synchronized void log(String logStatement, String uName, String serverToConnect) {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		 LocalDateTime tStamp = LocalDateTime.now(); 
		 String writeString = String.format("[%s] %s @ (Admin-%s) -- %s", dtf.format(tStamp), uName, serverToConnect.substring(1), logStatement);
		 try{
			File file = new File(String.format("admin_logs/%s.log", serverToConnect.substring(1)));
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
