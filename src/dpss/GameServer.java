package dpss;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GameServer { 
	
	public GameServer() {
		super();
	}
	
	
	public static void main(String[] args) {
        try {
        	System.out.println("NOTE -- " + "Server Logs available at " + System.getProperty("user.dir") + "/server_logs");
        	GameServerRMI gameServerNA = new CoreGameServer("NA");
        	GameServerRMI gameServerEU = new CoreGameServer("EU");
        	GameServerRMI gameServerAS = new CoreGameServer("AS");
        	ServerDiscovererRMI discoverer = new ServerDiscoverer();
			Registry reg = LocateRegistry.createRegistry(1098);
			reg.rebind("Discover", discoverer);
			reg.rebind("GameServerNA", gameServerNA);
			reg.rebind("GameServerEU", gameServerEU);
			reg.rebind("GameServerAS", gameServerAS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
