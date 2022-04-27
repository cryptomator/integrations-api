package org.cryptomator.integrations.uiappearance;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.util.Optional;

/**
 * This is the interface used by Cryptomator to get os specific UI appearances and themes.
 */
public interface UiAppearanceProvider {

	/**
	 * Loads the best-suited UiAppearanceProvider.
	 *
	 * @return preferred UiAppearanceProvider (if any)
	 * @since 1.1.0
	 */
	static Optional<UiAppearanceProvider> get() {
		return IntegrationsLoader.load(UiAppearanceProvider.class);
	}

	/**
	 * Gets the best-matching theme for the OS's current L&amp;F. This might be an approximation, as the OS might support more variations than we do.
	 *
	 * @implSpec Should default to {@link Theme#LIGHT} if the OS theme can't be determined, should not throw exceptions.
	 * @return The current OS theme
	 */
	Theme getSystemTheme();

	/**
	 * Adjusts parts of the UI to the desired theme, that can not be directly controlled from within Java.
	 * This might be required for window decorations or tray icons. Can be no-op.
	 *
	 * @implSpec A best-effort attempt should be made. If adjustments fail, do not throw an exception.
	 * @param theme What theme to adjust to
	 */
	void adjustToTheme(Theme theme);

	/**
	 * Registers a listener that gets notified when the system theme changes.
	 *
	 * @param listener The listener
	 * @throws UiAppearanceException If registering the listener failed.
	 */
	void addListener(UiAppearanceListener listener) throws UiAppearanceException;

	/**
	 * Removes a previously registered listener.
	 * <p>
	 * If the given listener has not been previously registered (i.e. it was never added) then this method call is a no-op.
	 *
	 * @param listener The listener
	 * @throws UiAppearanceException If removing the listener failed.
	 */
	void removeListener(UiAppearanceListener listener) throws UiAppearanceException;

}
