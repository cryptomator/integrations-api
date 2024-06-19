package org.cryptomator.integrations.sidebar;

public class SidebarServiceException extends Exception {

	public SidebarServiceException(String message) {
		super(message);
	}

	public SidebarServiceException(String message, Throwable t) {
		super(message, t);
	}
}
