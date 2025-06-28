package org.cryptomator.integrations.update;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.util.Optional;

/**
 * This is the interface used by Cryptomator to provide a way to update Cryptomator in a convinient way.
 * It's idependent of the supported platforms and package distribution channels.
 */
public interface UpdateService {

	static Optional<UpdateService> get() {
		return IntegrationsLoader.load(UpdateService.class);
	}

	enum DistributionChannel {
		LINUX_APPIMAGE,
		LINUX_AUR,
		LINUX_FLATPAK,
		LINUX_NIXOS,
		LINUX_PPA,
		MAC_BREW,
		MAC_DMG,
		WINDOWS_EXE,
		WINDOWS_MSI,
		WINDOWS_WINGET
	}

	/**
	 * @return <code>true</code> if this UppdateService can update the app.
	 * @implSpec This method must not throw any exceptions and should fail fast
	 * returning <code>false</code> if it's not possible to use this UppdateService
	 */
	boolean isSupported();

	/**
	 * Checks whether the update itself is already published on the given channel.
	 *
	 * @param channel The {@link DistributionChannel}  to check.
	 * @return        <code>null</code> if an update is not available, the version of the available update as String otherwise.
	 */
	String isUpdateAvailable(DistributionChannel channel);

	/**
	 * Trigger updating the app.
	 *
	 * @throws UpdateFailedException If the udpate wasn't successful or was cancelled.
	 */
	void triggerUpdate() throws UpdateFailedException;

	/**
	 * A flag indicating whether elevated permissions or sudo is required during update
	 * (so the user can be prepared for a corresponding prompt)
	 *
	 * @return <code>true</code> if elevated permissions are required, <code>false</code> otherwise.
	 */
	boolean doesRequireElevatedPermissions();

	/**
	 * Get a meaningful description of the update available to display it in the app
	 * like "Update via apt"
	 *
	 * @return The text to describes the update.
	 */
	String getDisplayName();
}
