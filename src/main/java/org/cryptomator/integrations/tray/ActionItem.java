package org.cryptomator.integrations.tray;

public record ActionItem(String title, Runnable action, boolean enabled) implements TrayMenuItem {

	public ActionItem(String title, Runnable action) {
		this(title, action, true);
	}
}
