package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import exceptions.UnknownServerRegionException;

public class GameServer { 
	
	private static GameServerRMI gameServerNA;
	private static GameServerRMI gameServerEU;
	private static GameServerRMI gameServerAS;

	public GameServer() {
		super();
	}
	
	public static void main(String[] args) {
        try {
        	
        	System.out.println("NOTE -- " + "Server Logs available at " + System.getProperty("user.dir") + "/server_logs");
        	gameServerNA = new CoreGameServer("NA");
        	gameServerEU = new CoreGameServer("EU");
        	gameServerAS = new CoreGameServer("AS");
        	
        	ServerDiscovererRMI discoverer = new ServerDiscoverer();
			Registry reg = LocateRegistry.createRegistry(1098);
			
			reg.rebind("Discover", discoverer);
			reg.rebind("GameServerNA", gameServerNA);
			reg.rebind("GameServerEU", gameServerEU);
			reg.rebind("GameServerAS", gameServerAS);
			
        } catch (UnknownServerRegionException e) {
        	e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
