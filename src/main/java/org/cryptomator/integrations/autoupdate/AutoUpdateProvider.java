package org.cryptomator.integrations.autoupdate;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.util.Optional;

/**
 * This is the interface used by Cryptomator to add autoupdate capabilities to the application.
 */
public interface AutoUpdateProvider {

	/**
	 * Loads the best-suited AutoUpdateProvider.
	 *
	 * @return preferred AutoUpdateProvider (if any)
	 * @since 1.2.0
	 */
	static Optional<AutoUpdateProvider> get() {
		return IntegrationsLoader.load(AutoUpdateProvider.class);
	}

	/**
	 * Initializes autoupdate features for the application. This sets configuration needed for autoupdate to work.
	 * The best place to do it is as soon after startup as possible, but no sooner than the app's main window is shown.
	 */
	void initAutoUpdate();

	/**
	 * Perform cleanups on the autoupdate features when the application exits.
	 */
	void cleanUpAutoUpdate();

	/**
	 * Enable autoupdate features for the applcation.
	 *
	 * @return True, in case enabling the features was successful, false otherwise.
	 * @throws AutoUpdateException If enabling autoupdate failed.
	 */
	boolean enableAutoUpdate() throws AutoUpdateException;

	/**
	 * Disable autoupdate features for the applcation.
	 *
	 * @return True, in case disabling the features was successful, false otherwise.
	 * @throws AutoUpdateException If disabling autoupdate failed.
	 */
	boolean disableAutoUpdate() throws AutoUpdateException;
}
