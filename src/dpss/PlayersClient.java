package dpss;

import java.rmi.Naming;

public class PlayersClient {

	public static void main(String[] args) {
		try {
			GameServerRMI stub = (GameServerRMI) Naming.lookup("rmi://127.0.0.1:1099" + "/AccountService");
			System.out.println(stub.hello());
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
//