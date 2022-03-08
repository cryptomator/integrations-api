package org.cryptomator.integrations.tray;

public class TrayMenuException extends Exception {

	public TrayMenuException(String message) {
		super(message);
	}

	public TrayMenuException(String message, Throwable cause) {
		super(message, cause);
	}

}