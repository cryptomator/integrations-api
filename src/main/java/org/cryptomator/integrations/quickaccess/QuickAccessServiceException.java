package org.cryptomator.integrations.quickaccess;

public class QuickAccessServiceException extends Exception {

	public QuickAccessServiceException(String message) {
		super(message);
	}

	public QuickAccessServiceException(String message, Throwable t) {
		super(message, t);
	}
}
