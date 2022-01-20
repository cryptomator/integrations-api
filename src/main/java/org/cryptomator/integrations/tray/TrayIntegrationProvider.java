package org.cryptomator.integrations.tray;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.util.Optional;

public interface TrayIntegrationProvider {

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
