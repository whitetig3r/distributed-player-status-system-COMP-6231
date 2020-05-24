package dpss;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameServerRMI extends Remote {
	public abstract String createPlayerAccount(String fName, String lName, String uName, String password, String ipAddress, int age) throws RemoteException;
	public abstract String playerSignIn(String uName, String password, String ipAddress) throws RemoteException;
	public abstract String playerSignOut(String uName, String ipAddress) throws RemoteException;
}