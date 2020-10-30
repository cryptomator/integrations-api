package org.cryptomator.integrations.autostart;

public class ToogleAutoStartFailedException extends Exception {

	public ToogleAutoStartFailedException(String message) {
		super(message);
	}

	public ToogleAutoStartFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}
