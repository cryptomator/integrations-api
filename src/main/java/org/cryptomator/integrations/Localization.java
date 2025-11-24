package org.cryptomator.integrations;

import java.util.ResourceBundle;

public enum Localization {
	INSTANCE;

	private final ResourceBundle resourceBundle = ResourceBundle.getBundle("IntegrationsApi");

	public static ResourceBundle get() {
		return INSTANCE.resourceBundle;
	}

}
