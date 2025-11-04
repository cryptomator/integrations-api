package org.cryptomator.integrations.update;

public interface UpdateInfo {

	static UpdateInfo of(String version, UpdateMechanism updateMechanism) {
		record UpdateInfoImpl(String version, UpdateMechanism updateMechanism) implements UpdateInfo {}
		return new UpdateInfoImpl(version, updateMechanism);
	}

	/**
	 * @return The version string of the available update.
	 */
	String version();

	/**
	 * @return The update mechanism that provided this update info.
	 */
	UpdateMechanism updateMechanism();
}
