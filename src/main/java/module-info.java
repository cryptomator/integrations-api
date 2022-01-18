import org.cryptomator.integrations.tray.AwtTrayMenuController;
import org.cryptomator.integrations.tray.TrayMenuController;

module org.cryptomator.integrations.api {
	requires java.desktop;

	exports org.cryptomator.integrations.autostart;
	exports org.cryptomator.integrations.keychain;
	exports org.cryptomator.integrations.tray;
	exports org.cryptomator.integrations.uiappearance;

	provides TrayMenuController with AwtTrayMenuController;
}