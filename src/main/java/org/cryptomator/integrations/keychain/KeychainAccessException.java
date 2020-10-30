package org.cryptomator.integrations.keychain;

public class KeychainAccessException extends Exception {

	public KeychainAccessException(String message) {
		super(message);
	}

	public KeychainAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}
