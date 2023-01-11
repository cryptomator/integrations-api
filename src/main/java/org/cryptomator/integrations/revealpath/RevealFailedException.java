package org.cryptomator.integrations.revealpath;

public class RevealFailedException extends Exception {

	public RevealFailedException(String msg) {
		super(msg);
	}

	public RevealFailedException(Exception cause) {
		super(cause);
	}

	public RevealFailedException(String msg, Exception cause) {
		super(msg, cause);
	}

}
