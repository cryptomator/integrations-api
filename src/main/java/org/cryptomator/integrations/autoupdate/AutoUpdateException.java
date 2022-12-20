package org.cryptomator.integrations.autoupdate;

public class AutoUpdateException extends Exception {

	public AutoUpdateException(String message) {
		super(message);
	}

	public AutoUpdateException(String message, Throwable cause) {
		super(message, cause);
	}
}
