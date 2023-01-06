package org.cryptomator.integrations.common;

public interface IntegrationService {


	/**
	 * Name of this service implementation.
	 *
	 * @return A human readable name of this service implementation
	 */
	String displayName();

	/**
	 * Indicates, if this service implemenation can be used.
	 *
	 * @return true, if this service implementation is supported in the current OS environment
	 * @implSpec This check needs to return fast and in constant time
	 */
	boolean isSupported();
}
