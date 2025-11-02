package org.cryptomator.integrations.update;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.cryptomator.integrations.common.NamedServiceProvider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpClient;
import java.util.Optional;

@ApiStatus.Experimental
public interface UpdateMechanism extends NamedServiceProvider {

	String UPDATE_MECHANISM_PROPERTY = "cryptomator.updateMechanism";

	static Optional<UpdateMechanism> get() {
		return Optional.ofNullable(System.getProperty(UPDATE_MECHANISM_PROPERTY))
				.flatMap(name -> IntegrationsLoader.loadSpecific(UpdateMechanism.class, name));
	}

	/**
	 * Checks whether an update is available by comparing the given version strings.
	 * @param updateVersion The version string of the update, e.g. "1.2.3".
	 * @param installedVersion The version string of the currently installed application, e.g. "1.2.3-beta4".
	 * @return <code>true</code> if an update is available, <code>false</code> otherwise. Always <code>true</code> for SNAPSHOT versions.
	 */
	static boolean isUpdateAvailable(String updateVersion, String installedVersion) {
		if (installedVersion.contains("SNAPSHOT")) {
			return true; // SNAPSHOT versions are always considered to be outdated.
		} else {
			return SemVerComparator.INSTANCE.compare(updateVersion, installedVersion) > 0;
		}
	}

	/**
	 * Checks whether an update is available.
	 * @param currentVersion The full version string of the currently installed application, e.g. "1.2.3-beta4".
	 * @param httpClient An HTTP client that can be used to check for updates.
	 * @return An {@link UpdateInfo} if an update is available, or <code>null</code> otherwise.
	 * @throws UpdateFailedException If the availability of an update could not be determined
	 */
	@Blocking
	@Nullable
	UpdateInfo checkForUpdate(String currentVersion, HttpClient httpClient) throws UpdateFailedException;

	/**
	 * Returns the first step to prepare the update. This can be anything like downloading the update, checking signatures, etc.
	 * @return a new {@link UpdateStep} that can be used to monitor the progress of the update preparation. The task will complete when the preparation is done.
	 * @throws UpdateFailedException If no update process can be started, e.g. due to network or I/O issues.
	 */
	UpdateStep firstStep() throws UpdateFailedException;

}