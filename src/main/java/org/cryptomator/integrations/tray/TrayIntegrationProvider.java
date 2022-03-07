package org.cryptomator.integrations.tray;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.util.Optional;

/**
 * Allows to perform OS-specific tasks when the app gets minimized to or restored from a tray icon.
 */
public interface TrayIntegrationProvider {

	/**
	 * Loads the best-suited TrayIntegrationProvider.
	 *
	 * @return preferred TrayIntegrationProvider (if any)
	 * @since 1.1.0
	 */
	static Optional<TrayIntegrationProvider> get() {
		return IntegrationsLoader.load(TrayIntegrationProvider.class);
	}

	/**
	 * Performs tasks required when the application is no longer showing any window and only accessible via
	 * system tray (or comparable facilities).
	 */
	void minimizedToTray();

	/**
	 * Performs tasks required when the application becomes "visible", i.e. is transitioning from a background task
	 * that lived in the system tray (or comparable facilities).
	 */
	void restoredFromTray();

}
