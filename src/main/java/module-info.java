import org.cryptomator.integrations.mount.MountService;
import org.cryptomator.integrations.revealpath.RevealPathService;
import org.cryptomator.integrations.secondfactor.SecondFactorProvider;
import org.cryptomator.integrations.tray.TrayMenuController;
import org.cryptomator.integrations.autostart.AutoStartProvider;
import org.cryptomator.integrations.keychain.KeychainAccessProvider;
import org.cryptomator.integrations.tray.TrayIntegrationProvider;
import org.cryptomator.integrations.uiappearance.UiAppearanceProvider;


module org.cryptomator.integrations.api {
	requires static org.jetbrains.annotations;
	requires org.slf4j;
	requires com.sun.jna;

	exports org.cryptomator.integrations.autostart;
	exports org.cryptomator.integrations.common;
	exports org.cryptomator.integrations.keychain;
	exports org.cryptomator.integrations.mount;
	exports org.cryptomator.integrations.revealpath;
	exports org.cryptomator.integrations.secondfactor;
	exports org.cryptomator.integrations.tray;
	exports org.cryptomator.integrations.uiappearance;

	uses AutoStartProvider;
	uses KeychainAccessProvider;
	uses MountService;
	uses RevealPathService;
	uses SecondFactorProvider;
	uses TrayIntegrationProvider;
	uses TrayMenuController;
	uses UiAppearanceProvider;
}