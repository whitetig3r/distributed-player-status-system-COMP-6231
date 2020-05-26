package exceptions;

public class UnknownServerRegionException extends Exception {

	public UnknownServerRegionException() {
		super("Region is unknown!");
	}

}
