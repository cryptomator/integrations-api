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

	/**
	 * With the exception of a provider-supplied default mount point, the mount point must be an existing dir.
	 * <p>
	 * This option is mutually exclusive with {@link #MOUNT_WITHIN_EXISTING_PARENT}.
	 *
	 * @see #DEFAULT_MOUNT_POINT
	 */
	MOUNT_TO_EXISTING_DIR,

	/**
	 * With the exception of a provider-supplied default mount point, the mount point must be a non-existing
	 * child within an existing parent.
	 * <p>
	 * This option is mutually exclusive with {@link #MOUNT_TO_EXISTING_DIR}.
	 *
	 * @see #DEFAULT_MOUNT_POINT
	 */
	MOUNT_WITHIN_EXISTING_PARENT,

	/**
	 * The mount point may be a drive letter.
	 *
	 * @see #MOUNT_TO_EXISTING_DIR
	 * @see #MOUNT_WITHIN_EXISTING_PARENT
	 */
	MOUNT_AS_DRIVE_LETTER,

	/**
	 * The provider supports suggesting a default mount point via {@link MountProvider#getDefaultMountPoint(String)}.
	 * <p>
	 * The default mount point is guaranteed to be supported by the mount builder, regardless of its normal restrictions.
	 */
	DEFAULT_MOUNT_POINT,

	/**
	 * The builder supports {@link MountBuilder#setReadOnly(boolean)}
	 */
	READ_ONLY,

	/**
	 * The mount supports {@link Mount#unmountForced()}.
	 */
	UNMOUNT_FORCED,

	/**
	 * The provider supports {@link MountProvider#getDefaultPort()}
	 * and the builder requires {@link MountBuilder#setPort(int)}.
	 */
	PORT
}
