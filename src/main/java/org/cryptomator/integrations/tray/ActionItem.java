package org.cryptomator.integrations.tray;

public record ActionItem(String title, Runnable action) implements TrayMenuItem {
}
