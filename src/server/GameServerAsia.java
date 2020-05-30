package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import exceptions.UnknownServerRegionException;

public class GameServerAsia {

	public GameServerAsia() {
		super();
	}

	public static void main(String[] args) {
		try {
			System.out.println("NOTE -- " + "Server Logs available at " + System.getProperty("user.dir") + "/server_logs/AS-server.log");
	    	GameServerRMI gameServerAS = new CoreGameServer("AS");
	    	Registry reg = LocateRegistry.createRegistry(1100);
	    	reg.rebind("GameServer", gameServerAS);
		} 
		catch (UnknownServerRegionException e) {
			e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

	}

}
