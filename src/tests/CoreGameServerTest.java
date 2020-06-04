package tests;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.UnknownServerRegionException;
import servers.CoreGameServer;
import servers.GameServerRMI;

public class CoreGameServerTest {
	@BeforeClass
	public static void setUp() throws RemoteException, UnknownServerRegionException, InterruptedException {
		GameServerRMI gameServerNA = new CoreGameServer("NA");
    	Registry reg = LocateRegistry.createRegistry(1098);
    	reg.rebind("GameServer", gameServerNA);
    	// wait for the server to come up
    	Thread.sleep(3000);
	}

	@Test
	public void threadedCreateAccountTest() throws MalformedURLException, RemoteException, NotBoundException {
		GameServerRMI serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",1098));
		String fName = "John";
		String lName = "Smith";
		String uName = "whitetig3r";
		String password = "Warrenleo97";
		String ipAddress = "132.168.2.22";
		int age = 23; 
		
	    CompletableFuture<String> threadOneJob = CompletableFuture.supplyAsync(()->{
	    	try {
				return serverStub.createPlayerAccount(fName, lName, uName, password, ipAddress, age);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	    	return null;
	    });

	    CompletableFuture<String> threadTwoJob = CompletableFuture.supplyAsync(()->{
	    	try {
				return serverStub.createPlayerAccount(fName, lName, uName, password, ipAddress, age);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	    	return null;
	    });


	    CompletableFuture<Void> allRetrieve = CompletableFuture.allOf(threadOneJob,threadTwoJob); 
	    
	    try {
	        allRetrieve.get();
	        ArrayList<String> retValues = Stream.of(threadOneJob,threadTwoJob)
	        		.map(CompletableFuture::join)
	        		.collect(Collectors.toCollection(ArrayList::new));
	        
	        assertTrue("Success!", retValues.contains(String.format("Successfully created account for player with username -- '%s'", uName)));      
	        assertTrue("It throws an error when trying to create an account with same uname again", retValues.contains("Player with that username already exists!"));

	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	@Test
	public void threadedPlayerSignInTest() throws MalformedURLException, RemoteException, NotBoundException {
		GameServerRMI serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",1098));
		String uName = "whitetig3r";
		String password = "Warrenleo97";
		String ipAddress = "132.168.2.22";
		
	    CompletableFuture<String> threadOneJob = CompletableFuture.supplyAsync(()->{
	    	try {
				return serverStub.playerSignIn(uName, password, ipAddress);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	    	return null;
	    });

	    CompletableFuture<String> threadTwoJob = CompletableFuture.supplyAsync(()->{
	    	try {
	    		return serverStub.playerSignIn(uName, password, ipAddress);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
	    	return null;
	    });
	    

	    CompletableFuture<Void> allRetrieve = CompletableFuture.allOf(threadOneJob,threadTwoJob); 
	    
	    try {
	        allRetrieve.get();
	        ArrayList<String> retValues = Stream.of(threadOneJob,threadTwoJob)
	        		.map(CompletableFuture::join)
	        		.collect(Collectors.toCollection(ArrayList::new));
	        
	        assertTrue("Success!", retValues.contains(String.format("Successfully signed in player with username -- '%s'",uName)));      
	        assertTrue("It throws an error when trying to sign in again", retValues.contains(String.format("Player '%s' is already signed in", uName)));
	        
	        serverStub.playerSignOut(uName, ipAddress);
	        String badSignIn = serverStub.playerSignIn(uName, "BADPASS", ipAddress);
	        assertTrue("It throws an error for bad uname & password combination!", badSignIn.equals(String.format("Player with username '%s' and that password combination does not exist", uName)));

	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	@Test
	public void threadedGetPlayerStatus() throws MalformedURLException, RemoteException, NotBoundException, UnknownServerRegionException, InterruptedException {
		GameServerRMI serverStub = (GameServerRMI) Naming.lookup(String.format("rmi://127.0.0.1:%d/GameServer",1098));
		GameServerRMI gameServerEU = new CoreGameServer("EU");
    	Registry regEU = LocateRegistry.createRegistry(1099);
    	regEU.rebind("GameServer", gameServerEU);
    	// wait for the server to come up
    	Thread.sleep(3000);
    	GameServerRMI gameServerAS = new CoreGameServer("AS");
    	Registry regAS = LocateRegistry.createRegistry(1100);
    	regAS.rebind("GameServer", gameServerAS);
    	// wait for the server to come up
    	Thread.sleep(3000);
    	
		String uName = "Admin";
		String password = "Admin";
		String ipAddress = "132.168.2.22";
		final String expectedNA = "NA: Online: 0 Offline: 4";
		final String expectedEU = "EU: Online: 0 Offline: 3";
		final String expectedAS = "AS: Online: 0 Offline: 3";
		
		serverStub.adminSignIn(uName, password, ipAddress);
		
	    CompletableFuture<String> threadOneJob = CompletableFuture.supplyAsync(()->{
	    	try {
				return serverStub.getPlayerStatus(uName, password, ipAddress);
			} catch (RemoteException | UnknownServerRegionException e) {
				e.printStackTrace();
			}
	    	return null;
	    });

	    CompletableFuture<String> threadTwoJob = CompletableFuture.supplyAsync(()->{
	    	try {
	    		return serverStub.getPlayerStatus(uName, password, ipAddress);
			} catch (RemoteException | UnknownServerRegionException e) {
				e.printStackTrace();
			}
	    	return null;
	    });
	    

	    CompletableFuture<Void> allRetrieve = CompletableFuture.allOf(threadOneJob,threadTwoJob); 
	    
	    try {
	        allRetrieve.get();
	        ArrayList<String> retValues = Stream.of(threadOneJob,threadTwoJob)
	        		.map(CompletableFuture::join)
	        		.collect(Collectors.toCollection(ArrayList::new));
	        
	        assertTrue("It contains the right stats for NA server -- One Thread", retValues.get(0).contains(expectedNA));
	        assertTrue("It contains the right stats for NA server -- Second Thread", retValues.get(1).contains(expectedNA));
	        
	        assertTrue("It contains the right stats for EU server -- One Thread", retValues.get(0).contains(expectedEU));
	        assertTrue("It contains the right stats for EU server -- Second Thread", retValues.get(1).contains(expectedEU));
	        
	        assertTrue("It contains the right stats for AS server -- One Thread", retValues.get(0).contains(expectedAS));
	        assertTrue("It contains the right stats for AS server -- Second Thread", retValues.get(1).contains(expectedAS));

	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

}
