package dpss;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameServerRMI extends Remote {
	public abstract String hello() throws RemoteException;
}