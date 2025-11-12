import org.cryptomator.integrations.quickaccess.QuickAccessService;
import org.cryptomator.integrations.mount.MountService;
import org.cryptomator.integrations.revealpath.RevealPathService;
import org.cryptomator.integrations.tray.TrayMenuController;
import org.cryptomator.integrations.autostart.AutoStartProvider;
import org.cryptomator.integrations.keychain.KeychainAccessProvider;
import org.cryptomator.integrations.tray.TrayIntegrationProvider;
import org.cryptomator.integrations.uiappearance.UiAppearanceProvider;
import org.cryptomator.integrations.update.UpdateMechanism;


module org.cryptomator.integrations.api {
	requires static org.jetbrains.annotations;
	requires org.slf4j;
	requires com.fasterxml.jackson.databind;
	requires java.net.http;

	exports org.cryptomator.integrations.autostart;
	exports org.cryptomator.integrations.common;
	exports org.cryptomator.integrations.keychain;
	exports org.cryptomator.integrations.mount;
	exports org.cryptomator.integrations.revealpath;
	exports org.cryptomator.integrations.tray;
	exports org.cryptomator.integrations.uiappearance;
	exports org.cryptomator.integrations.quickaccess;
	exports org.cryptomator.integrations.update;

	uses AutoStartProvider;
	uses KeychainAccessProvider;
	uses MountService;
	uses RevealPathService;
	uses TrayIntegrationProvider;
	uses TrayMenuController;
	uses UiAppearanceProvider;
	uses QuickAccessService;
	uses UpdateMechanism;
}