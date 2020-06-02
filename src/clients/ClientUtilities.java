package clients;

import java.util.Scanner;

public class ClientUtilities {
	private static Scanner sc = new Scanner(System.in);
	
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
		
		String ipAddress = sc.nextLine();
		while (ClientUtilities.getRegionServer(ipAddress) < 0) {
			if(ClientUtilities.getRegionServer(ipAddress) == -1) {
				System.out.println("ERROR: Server for this region does not exist!");
			} else {
				System.out.println("ERROR: IP Address in invalid!");
			}
			System.out.println("Enter IP Address:");
			ipAddress = sc.nextLine();
		}
		return ipAddress;
	}
	
	public static String getSafeStringInput(String prompt) {
		while(true) {
			System.out.println(prompt);
			String inpVal = sc.nextLine();
			if(inpVal.length() == 0) {
				System.out.println("ERROR: Empty input!");
				continue;
			}
			if(inpVal.trim().length() < inpVal.length()) {
				System.out.println("ERROR: Cannot contain leading or trailing spaces!");
				continue;
			}
			return inpVal;
		}
		
	}
	
	public static int getSafeIntInput(String prompt) {
		while(true) {
			try {
				System.out.println(prompt);
				int inpVal = Integer.parseInt(sc.nextLine());
				return inpVal;
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Not a valid number!");
			    continue;
			}
		}
		
	}

	public static String getServerName(int registryPort) {
		switch(registryPort) {
			case 1098:
				return "/NA";
			case 1099:
				return "/EU";
			case 1100:
				return "/AS";
			default:
				return "/UNKNOWN";
		}
	}
}
