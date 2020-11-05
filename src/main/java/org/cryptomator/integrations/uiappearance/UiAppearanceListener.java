package org.cryptomator.integrations.uiappearance;

@FunctionalInterface
public interface UiAppearanceListener {

	void systemAppearanceChanged(Theme newTheme);

}
