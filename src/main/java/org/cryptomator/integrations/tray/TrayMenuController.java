package org.cryptomator.integrations.tray;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Displays a tray icon and menu
 *
 * @since 1.1.0
 */
@ApiStatus.Experimental
public interface TrayMenuController {

	static Optional<TrayMenuController> get() {
		return IntegrationsLoader.load(TrayMenuController.class);
	}

	/**
	 * Displays an icon on the system tray.
	 *
	 * @param imageData     What image to show
	 * @param defaultAction Action to perform when interacting with the icon directly instead of its menu
	 * @param tooltip       Text shown when hovering
	 * @throws TrayMenuException thrown when adding the tray icon failed
	 */
	void showTrayIcon(byte[] imageData, Runnable defaultAction, String tooltip) throws TrayMenuException;

	/**
	 * Updates the icon on the system tray.
	 *
	 * @param imageData What image to show
	 * @throws IllegalStateException thrown when called before an icon has been added
	 */
	void updateTrayIcon(byte[] imageData);

	/**
	 * Show the given options in the tray menu.
	 * <p>
	 * This method may be called multiple times, e.g. when the vault list changes.
	 *
	 * @param items Menu items
	 * @throws TrayMenuException thrown when updating the tray menu failed
	 */
	void updateTrayMenu(List<TrayMenuItem> items) throws TrayMenuException;

	/**
	 * Action to run before the tray menu opens.
	 * <p>
	 * This method is used to set up an event listener for when the menu is opened,
	 * e.g. so that the vault list can be updated to reflect volume mount state changes
	 * which occur while Cryptomator is in the system tray (and not open).
	 *
	 * @param listener
	 * @throws IllegalStateException thrown when adding listeners fails (i.e. there's no tray menu)
	 */
	void onBeforeOpenMenu(Runnable listener);

}
