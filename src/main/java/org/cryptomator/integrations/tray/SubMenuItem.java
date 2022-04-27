package org.cryptomator.integrations.tray;

import java.util.List;

public record SubMenuItem(String title, List<TrayMenuItem> items) implements TrayMenuItem {
}
