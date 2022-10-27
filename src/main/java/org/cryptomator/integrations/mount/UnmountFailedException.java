package org.cryptomator.integrations.mount;

public class UnmountFailedException extends Exception {

	public UnmountFailedException(String msg) {
		super(msg);
	}

	public UnmountFailedException(Exception cause) {
		super(cause);
	}

	public UnmountFailedException(String msg, Exception cause) {
		super(msg, cause);
	}
}
