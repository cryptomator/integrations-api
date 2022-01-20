package org.cryptomator.integrations.tray;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface TrayMenuController {

	static Optional<TrayMenuController> get() {
		return IntegrationsLoader.load(TrayMenuController.class);
	}

	/**
	 * Adds an icon to the system tray.
	 *
	 * @param iconData What image to show
	 * @param tooltip  Text shown when hovering
	 * @throws IOException thrown when interacting with the given <code>iconData</code>
	 */
	void showTrayIcon(InputStream iconData, String tooltip) throws IOException;

	/**
	 * Show the given options in the tray menu.
	 *
	 * @param items Menu items
	 */
	void setTrayMenu(List<TrayMenuItem> items);

}
