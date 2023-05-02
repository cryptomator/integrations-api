package org.cryptomator.integrations.tray;

/**
 * A callback used by the {@link TrayMenuController} to load tray icons in the format required by the implementation.
 */
sealed public interface TrayIconLoader permits TrayIconLoader.PngData, TrayIconLoader.FreedesktopIconName {

	non-sealed interface PngData extends TrayIconLoader {

		/**
		 * Loads an icon from a byte array holding a loaded PNG file.
		 *
		 * @param data png data
		 */
		void loadPng(byte[] data);
	}

	non-sealed interface FreedesktopIconName extends TrayIconLoader {

		/**
		 * Loads an icon by looking it up {@code iconName} inside of {@code $XDG_DATA_DIRS/icons}.
		 *
		 * @param iconName the icon name
		 */
		void lookupByName(String iconName);
	}
}
