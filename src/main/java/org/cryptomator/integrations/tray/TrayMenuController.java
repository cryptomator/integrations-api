package org.cryptomator.integrations.tray;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Displays a tray icon and menu
 *
 * @since 1.1.0
 */
public interface TrayMenuController {

	static Optional<TrayMenuController> get() {
		return IntegrationsLoader.load(TrayMenuController.class);
	}

	/**
	 * Displays an icon on the system tray.
	 *
	 * @param rawImageData  What image to show
	 * @param defaultAction Action to perform when interacting with the icon directly instead of its menu
	 * @param tooltip       Text shown when hovering
	 * @throws IOException thrown when interacting with the given <code>rawImageData</code>
	 */
	void showTrayIcon(InputStream rawImageData, Runnable defaultAction, String tooltip) throws IOException;

	/**
	 * Show the given options in the tray menu.
	 * <p>
	 * This method may be called multiple times, e.g. when the vault list changes.
	 *
	 * @param items Menu items
	 */
	void updateTrayMenu(List<TrayMenuItem> items);

}
