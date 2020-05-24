package dpss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.file.attribute.FileTime;

public class GameServer { 
	private static String serverDetailsFile;
	
	private static final Path na = Paths.get("NA.txt");
	private static final Path eu = Paths.get("EU.txt");
	private static final Path as = Paths.get("AS.txt");
	
	public GameServer() {
		super();
	}
	
	private static void setFileToWrite() throws IOException {
		FileTime na_time = Files.exists(na) ? Files.getLastModifiedTime(na) : null;
		FileTime eu_time = Files.exists(eu) ? Files.getLastModifiedTime(eu) : null;
		FileTime as_time = Files.exists(as) ? Files.getLastModifiedTime(as) : null;
		
		if(na_time != null && eu_time != null && as_time != null) {
			ArrayList<FileTime> timestamps = new ArrayList<>();
			timestamps.add(na_time);
			timestamps.add(eu_time);
			timestamps.add(as_time);
			
			Collections.sort(timestamps);
			
			if(timestamps.get(0).equals(na_time)) serverDetailsFile = "NA.txt";
			else if (timestamps.get(0).equals(eu_time)) serverDetailsFile = "EU.txt";
			else serverDetailsFile = "AS.txt"; 
		
		} else if(na_time == null) {
			serverDetailsFile = "NA.txt";
		} else if(eu_time == null) {
			serverDetailsFile = "EU.txt";
		} else if(as_time == null) {
			serverDetailsFile = "AS.txt";
		} else {
			System.out.println("ERROR!");
		}
	}
	
	private static void writeToRepo(String defaultPort) throws IOException {	
		setFileToWrite();
	    BufferedWriter writer = new BufferedWriter(new FileWriter(serverDetailsFile));
	    writer.write(defaultPort);
	    System.out.println("Written Server Details to -- "+ serverDetailsFile);
	    writer.close();
	}
	
	public static void createAnotherRegistry(int defaultPort) throws IOException {
		try {
        	GameServerRMI accountService = new Account();
			Registry reg = LocateRegistry.createRegistry(defaultPort);
			reg.rebind("AccountService", accountService);
			writeToRepo(String.valueOf(defaultPort));

		} catch(ExportException e) {
			createAnotherRegistry(defaultPort+1);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
        try {
        	System.setProperty("java.rmi.server.hostname","127.0.0.1");
        	createAnotherRegistry(1098);
        	
        } catch (Exception e) {
            System.err.println("exception:");
            e.printStackTrace();
        }
    }

}
