package org.cryptomator.integrations.mount;

public class MountFailedException extends Exception {

	public MountFailedException(String msg) {
		super(msg);
	}

	public MountFailedException(Exception cause) {
		super(cause);
	}

	public MountFailedException(String msg, Exception cause) {
		super(msg, cause);
	}
}
