package servers;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import exceptions.UnknownServerRegionException;

public class GameServerNorthAmerica {

	public GameServerNorthAmerica() {
		super();
	}

	public static void main(String[] args) {
		try {
			System.out.println("NOTE -- " + "Server Logs available at " + System.getProperty("user.dir") + "/server_logs/NA-server.log");
	    	GameServerRMI gameServerNA = new CoreGameServer("NA");
	    	Registry reg = LocateRegistry.createRegistry(1098);
	    	reg.rebind("GameServer", gameServerNA);
		} 
		catch (UnknownServerRegionException e) {
			e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

}
