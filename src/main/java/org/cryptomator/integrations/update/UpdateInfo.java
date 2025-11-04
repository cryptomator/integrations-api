package org.cryptomator.integrations.update;

import org.jetbrains.annotations.NotNull;

public interface UpdateInfo<T extends UpdateInfo<T>> {

	/**
	 * @return The version string of the available update.
	 */
	String version();

	/**
	 * @return The update mechanism that provided this update info.
	 */
	UpdateMechanism<T> updateMechanism();

	/**
	 * Typesafe equivalent to {@code updateMechanism().firstStep(this)}.
	 * @return Result of {@link UpdateMechanism#firstStep(UpdateInfo)}.
	 * @throws UpdateFailedException If no update process can be started, e.g. due to network or I/O issues.
	 */
	@NotNull
	default UpdateStep useToPrepareFirstStep() throws UpdateFailedException {
		@SuppressWarnings("unchecked") T self = (T) this;
		return updateMechanism().firstStep(self);
	}
}
