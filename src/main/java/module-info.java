import org.cryptomator.integrations.mount.MountService;
import org.cryptomator.integrations.tray.TrayMenuController;
import org.cryptomator.integrations.autostart.AutoStartProvider;
import org.cryptomator.integrations.autoupdate.AutoUpdateProvider;
import org.cryptomator.integrations.keychain.KeychainAccessProvider;
import org.cryptomator.integrations.tray.TrayIntegrationProvider;
import org.cryptomator.integrations.uiappearance.UiAppearanceProvider;


module org.cryptomator.integrations.api {
	requires static org.jetbrains.annotations;
	requires org.slf4j;

	exports org.cryptomator.integrations.autostart;
	exports org.cryptomator.integrations.autoupdate;
	exports org.cryptomator.integrations.common;
	exports org.cryptomator.integrations.keychain;
	exports org.cryptomator.integrations.mount;
	exports org.cryptomator.integrations.tray;
	exports org.cryptomator.integrations.uiappearance;

	uses AutoStartProvider;
	uses AutoUpdateProvider;
	uses KeychainAccessProvider;
	uses MountService;
	uses TrayIntegrationProvider;
	uses TrayMenuController;
	uses UiAppearanceProvider;
}