package org.cryptomator.integrations.tray;

public interface TrayIntegrationProvider {

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
