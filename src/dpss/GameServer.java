package dpss;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameServer { 
	
	private static GameServerRMI gameServerNA;
	private static GameServerRMI gameServerEU;
	private static GameServerRMI gameServerAS;

	public GameServer() {
		super();
	}
	
	public static String retrievePlayerStatuses()
	{
	    CompletableFuture<String> naRetrieve = CompletableFuture.supplyAsync(()->{
	        	CoreGameServer server = (CoreGameServer) gameServerNA;
				return server.getPlayerCounts();
	    });

	    CompletableFuture<String> euRetrieve = CompletableFuture.supplyAsync(()->{
	    	CoreGameServer server = (CoreGameServer) gameServerEU;
			return server.getPlayerCounts();
	    });

	    CompletableFuture<String> asRetrieve = CompletableFuture.supplyAsync(()->{
	    	CoreGameServer server = (CoreGameServer) gameServerAS;
			return server.getPlayerCounts();
	    });

	    CompletableFuture<Void> allRetrieve = CompletableFuture.allOf(naRetrieve, euRetrieve, asRetrieve); 
	    
	    try {
	        allRetrieve.get();
	        return Stream.of(naRetrieve, euRetrieve, asRetrieve)
	        		  .map(CompletableFuture::join)
	        		  .collect(Collectors.joining("\n"));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return "UNRECOGNIZED FAILURE!";
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
			listenForPlayerStatusRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	private static void listenForPlayerStatusRequests() {
		DatagramSocket aSocket = null;
		try{
	    	aSocket = new DatagramSocket(6789);
			byte[] buffer = new byte[1000];
			System.out.println("Starting UDP Server on port 6789...");
 			while(true){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);     
  				String toSend = retrievePlayerStatuses();
    			DatagramPacket reply = new DatagramPacket(toSend.getBytes(), toSend.getBytes().length, request.getAddress(), request.getPort());
    			aSocket.send(reply);
    		}
		} catch (SocketException e){
		 	System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if(aSocket != null) aSocket.close();
		}
	}

}
