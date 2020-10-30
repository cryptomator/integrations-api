package org.cryptomator.integrations.autostart;

public class ToggleAutoStartFailedException extends Exception {

	public ToggleAutoStartFailedException(String message) {
		super(message);
	}

	public ToggleAutoStartFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}
