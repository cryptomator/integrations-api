package org.cryptomator.integrations.uiappearance;

/**
 * This is the interface used by Cryptomator to get os specific UI appearances and themes.
 */
public interface UiAppearanceProvider {

	Theme getCurrentTheme() throws UiAppearanceException;

	void addListener() throws UiAppearanceException;
	void removeListener() throws UiAppearanceProvider;

}
