package org.cryptomator.integrations.secondfactor;

import org.cryptomator.integrations.common.IntegrationsLoader;

import com.sun.jna.Callback;
import java.util.Optional;

/**
 * Allows to request and use an authentication device as a 2nd factor.
 */
public interface SecondFactorProvider {

	/**
	 * Loads the best-suited SecondFactorProvider.
	 *
	 * @return preferred SecondFactorProvider (if any).
	 * @since 1.4.0 //ToDo fix version
	 */
	static Optional<SecondFactorProvider> get() {
		return IntegrationsLoader.load(SecondFactorProvider.class);
	}

	/**
	 * Check, whether an authentication device is available and ready to use.
	 * @return	<code>true</code>, in case an authentication device is available and ready to us, <code>false</code> otherwise.
	 */
	boolean device_supported();

	/**
	 * Authenticate using the authentication device.
	 *
	 * @param message	Reason for authenticating, gets displayed by the device, if it supports it.
	 * @param callback  Callback, containing the result, whether authentication was successful or not.
	 */
	void device_authenticate(String message, Callback callback);
}
