package clients;

import java.util.Scanner;

import exceptions.UnknownServerRegionException;

public class ClientUtilities {
	
	private static boolean isValidIP(String ipAddr) {
		String[] groups = ipAddr.split("\\.");

		if (groups.length != 4) {
			return false;
		}
		
		return true;
	}
	
	public static int getRegionServer(String ipAddress) {
		if(isValidIP(ipAddress)) {
			if(ipAddress.startsWith("132")) {
				return 1098;
			} else if (ipAddress.startsWith("93")) {
				return 1099;
			} else if (ipAddress.startsWith("182")) {
				return 1100;
			} else return -1;
		} 
		return -2;
	}
	
	
	public static String getIpAddressInput() {
		Scanner sc = new Scanner(System.in);
		String ipAddress = sc.nextLine();
		while (ClientUtilities.getRegionServer(ipAddress) < 0) {
			if(ClientUtilities.getRegionServer(ipAddress) == -1) {
				System.out.println("Server for this region does not exist!");
			} else {
				System.out.println("IP Address in invalid!");
			}
			System.out.println("Enter IP Address:");
			ipAddress = sc.nextLine();
		}
		return ipAddress;
	}
}
