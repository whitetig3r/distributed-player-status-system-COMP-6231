package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import exceptions.UnknownServerRegionException;

public class GameServerEurope {

	public GameServerEurope() {
		super();
	}

	public static void main(String[] args) {
		try {
			System.out.println("NOTE -- " + "Server Logs available at " + System.getProperty("user.dir") + "/server_logs/EU-server.log");
	    	GameServerRMI gameServerEU = new CoreGameServer("EU");
	    	Registry reg = LocateRegistry.createRegistry(1099);
	    	reg.rebind("GameServer", gameServerEU);
		} 
		catch (UnknownServerRegionException e) {
			e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

	}

}
