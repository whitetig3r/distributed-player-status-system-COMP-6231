package dpss;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Account extends UnicastRemoteObject implements GameServerRMI {

	private static final long serialVersionUID = -3393990750400755075L;

	protected Account() throws RemoteException {
		super();
	}
	
	public String hello() {
		return "Hello from Accounts Service!";
	}

}
