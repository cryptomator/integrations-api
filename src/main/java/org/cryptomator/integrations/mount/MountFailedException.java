package org.cryptomator.integrations.mount;

public class MountFailedException extends Exception {

	public MountFailedException(Exception e) {
		super(e);
	}
}
