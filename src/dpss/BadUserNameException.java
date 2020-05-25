package dpss;

public class BadUserNameException extends Exception {
	public BadUserNameException() {
		super("A Valid username should have between 6 and 15 characters");
	}

}
