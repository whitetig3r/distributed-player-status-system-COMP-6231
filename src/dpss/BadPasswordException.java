package dpss;

public class BadPasswordException extends Exception {

	public BadPasswordException() {
		super("A valid password must have a minimum of 6 characters!");
	}

}
