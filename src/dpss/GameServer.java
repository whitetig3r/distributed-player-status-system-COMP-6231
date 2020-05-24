package dpss;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GameServer { 
	
	public GameServer() {
		super();
	}
	
	
	public static void main(String[] args) {
        try {
        	GameServerRMI gameServerNA = new CoreGameServer();
        	GameServerRMI gameServerEU = new CoreGameServer();
        	GameServerRMI gameServerAS = new CoreGameServer();
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
