package org.cryptomator.integrations.mount;

/**
 * Describes what aspects of the mount implementation can or should be used.
 * <p>
 * This may be used to show or hide different configuration options depending on the chosen mount provider.
 */
public enum MountFeature {
	/**
	 * The provider supports {@link MountProvider#getDefaultMountFlags(String)}
	 * and the builder requires {@link MountBuilder#setMountFlags(String)}.
	 */
	MOUNT_FLAGS,
	MOUNT_POINT_EMPTY_DIR,
	MOUNT_POINT_DRIVE_LETTER,
	MOUNT_POINT_PATH_PREFIX,

	/**
	 * The builder supports {@link MountBuilder#setReadOnly(boolean)}
	 */
	READ_ONLY,

	/**
	 * The mount supports {@link Mount#unmountForced()}.
	 */
	UNMOUNT_FORCED,
	ON_EXIT_ACTION,

	/**
	 * The provider supports {@link MountProvider#getDefaultPort()}
	 * and the builder requires {@link MountBuilder#setPort(int)}.
	 */
	PORT,

	/**
	 * The provider supports {@link MountProvider#getDefaultMountPoint(String)}
	 */
	DEFAULT_MOUNT_POINT
}
