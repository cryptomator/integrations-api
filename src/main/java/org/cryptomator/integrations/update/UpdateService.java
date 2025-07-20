package org.cryptomator.integrations.update;

import org.cryptomator.integrations.common.DistributionChannel;
import org.cryptomator.integrations.common.IntegrationsLoader;

import java.util.stream.Stream;

/**
 * This is the interface used by Cryptomator to provide a way to update Cryptomator in a convinient way.
 * It's idependent of the supported platforms and package distribution channels.
 */
public interface UpdateService {

	static Stream<UpdateService> get() {
		return IntegrationsLoader.loadAll(UpdateService.class).filter(UpdateService::isSupported);
	}

	/**
	 * @return <code>true</code> if this UppdateService can update the app.
	 * @implSpec This method must not throw any exceptions and should fail fast
	 * returning <code>false</code> if it's not possible to use this UppdateService
	 */
	boolean isSupported();

	/**
	 * Retrieve an object to check for the latest release published on the given channel.
	 *
	 * @param channel The DistributionChannel.Value to check.
	 * @return        An object that is capable of checking asynchronously for the latest release.
	 */
	Object getLatestReleaseChecker(DistributionChannel.Value channel);

	/**
	 * Trigger updating the app.
	 *
	 * @throws UpdateFailedException If the udpate wasn't successful or was cancelled.
	 */
	void triggerUpdate() throws UpdateFailedException;

	/**
	 * Start a new instance of the application.
	 *
	 * @return The PID of the new process.
	 */
	long spawnApp();

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
