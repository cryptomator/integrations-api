package org.cryptomator.integrations.autolock;

public class AutoLockException extends Exception{

	public AutoLockException(String message) {
		super(message);
	}

	public AutoLockException(String message, Throwable cause) {
		super(message, cause);
	}

}
