package org.cryptomator.integrations.update;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.cryptomator.integrations.common.NamedServiceProvider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;

@ApiStatus.Experimental
public interface UpdateMechanism extends NamedServiceProvider {

	static UpdateMechanism get() {
		// TODO: load preferred udpate mechanism, if specified in system properties.
		return IntegrationsLoader.load(UpdateMechanism.class).orElseThrow(); // Fallback "show download page" mechanism always available.
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
	 * @return <code>true</code> if an update is available, <code>false</code> otherwise.
	 * @throws UpdateFailedException If the availability of an update could not be determined
	 */
	@Blocking
	boolean isUpdateAvailable(String currentVersion) throws UpdateFailedException;

	/**
	 * Performs as much as possible to prepare the update. This may include downloading the update, checking signatures, etc.
	 * @return a new {@link UpdateProcess} that can be used to monitor the progress of the update preparation. The task will complete when the preparation is done.
	 * @throws UpdateFailedException If no update process can be started, e.g. due to network or I/O issues.
	 */
	UpdateProcess prepareUpdate() throws UpdateFailedException;

}