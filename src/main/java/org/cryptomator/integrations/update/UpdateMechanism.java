package org.cryptomator.integrations.update;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.cryptomator.integrations.common.NamedServiceProvider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;

@ApiStatus.Experimental
public interface UpdateMechanism extends NamedServiceProvider {

	static UpdateMechanism get() {
		return IntegrationsLoader.load(UpdateMechanism.class).orElseThrow(); // Fallback "show download page" mechanism always available.
	}

	/**
	 * Checks whether an update is available.
	 * @return <code>true</code> if an update is available, <code>false</code> otherwise.
	 */
	@Blocking
	boolean isUpdateAvailable(); // TODO: let it throw?

	/**
	 * Performs as much as possible to prepare the update. This may include downloading the update, checking signatures, etc.
	 * @return a new {@link UpdateProcess} that can be used to monitor the progress of the update preparation. The task will complete when the preparation is done.
	 * @throws UpdateFailedException If no update process can be started, e.g. due to network or I/O issues.
	 */
	UpdateProcess prepareUpdate() throws UpdateFailedException;

}