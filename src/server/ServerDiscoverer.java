package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerDiscoverer extends UnicastRemoteObject implements ServerDiscovererRMI {

	private static final long serialVersionUID = -6818301376839630444L;

	protected ServerDiscoverer() throws RemoteException {
		super();
	}

	private static boolean isValidIP(String ipAddr) {
		String[] groups = ipAddr.split("\\.");

		if (groups.length != 4) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String getRegionServer(String ipAddress) throws RemoteException {
		if(isValidIP(ipAddress)) {
			if(ipAddress.startsWith("132")) {
				return  "/GameServerNA";
			} else if (ipAddress.startsWith("93")) {
				return "/GameServerEU";
			} else if (ipAddress.startsWith("182")) {
				return "/GameServerAS";
			}
		} 
		return null;
	}

}
