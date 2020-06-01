package servers;

import java.rmi.Remote;
import java.rmi.RemoteException;

import exceptions.UnknownServerRegionException;

public interface GameServerRMI extends Remote {
	public abstract String createPlayerAccount(String fName, String lName, String uName, String password, String ipAddress, int age) throws RemoteException;
	public abstract String playerSignIn(String uName, String password, String ipAddress) throws RemoteException;
	public abstract String playerSignOut(String uName, String ipAddress) throws RemoteException;
	public abstract String adminSignIn(String uName, String password, String ipAddress) throws RemoteException;
	public abstract String adminSignOut(String uName, String ipAddress) throws RemoteException;
	public abstract String getPlayerStatus(String uName, String password, String ipAddress) throws RemoteException, UnknownServerRegionException;
}