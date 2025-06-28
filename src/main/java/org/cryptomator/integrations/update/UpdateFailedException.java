package org.cryptomator.integrations.update;

public class UpdateFailedException extends Exception {

	public UpdateFailedException(String message) {
		super(message);
	}

	public UpdateFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
