package org.cryptomator.integrations.mount;

public class UnmountFailedException extends Exception {

	public UnmountFailedException() {
		super();
	}

	public UnmountFailedException(Exception e) {
		super(e);
	}
}
