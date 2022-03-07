package org.cryptomator.integrations.tray;

public sealed interface TrayMenuItem permits ActionItem, SubMenuItem, SeparatorItem {
}
