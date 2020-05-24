package dpss;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class PlayersClient {

	private static Scanner sc = new Scanner(System.in);
	private static String serverToConnect;
	private static GameServerRMI serverStub;
	private static ServerDiscovererRMI discoveryStub;
	
	

	public static void main(String[] args) {
		try {
			createPlayerAccount();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private static void realizeCreatePlayerAccount(String fName, String lName, String uName, String password, int age, String ipAddress) throws MalformedURLException, RemoteException, NotBoundException {
		discoveryStub = (ServerDiscovererRMI) Naming.lookup("rmi://127.0.0.1:1098/Discover");
		serverToConnect = discoveryStub.getRegionServer(ipAddress);
		
		System.out.println(serverToConnect);
		
		if(serverToConnect != null) {
			serverStub = (GameServerRMI) Naming.lookup("rmi://127.0.0.1:1098" + serverToConnect);
			System.out.println(serverStub.createPlayerAccount(fName, lName, uName, password, ipAddress, age));
		} else {
			System.out.println("Bad IP Address!");
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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}