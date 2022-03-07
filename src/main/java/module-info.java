import org.cryptomator.integrations.tray.TrayMenuController;
import org.cryptomator.integrations.autostart.AutoStartProvider;
import org.cryptomator.integrations.keychain.KeychainAccessProvider;
import org.cryptomator.integrations.tray.TrayIntegrationProvider;
import org.cryptomator.integrations.uiappearance.UiAppearanceProvider;


module org.cryptomator.integrations.api {
	requires static org.jetbrains.annotations;

	exports org.cryptomator.integrations.autostart;
	exports org.cryptomator.integrations.common;
	exports org.cryptomator.integrations.keychain;
	exports org.cryptomator.integrations.tray;
	exports org.cryptomator.integrations.uiappearance;

	uses AutoStartProvider;
	uses KeychainAccessProvider;
	uses TrayIntegrationProvider;
	uses TrayMenuController;
	uses UiAppearanceProvider;
}