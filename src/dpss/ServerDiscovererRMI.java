package dpss;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerDiscovererRMI extends Remote {
	public abstract String getRegionServer(String ipAddress) throws RemoteException;
}
