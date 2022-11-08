package org.cryptomator.integrations.mount;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

import java.nio.file.Path;

/**
 * Builder to mount a filesystem.
 * <p>
 * The setter may attempt to validate the input, but {@link #mount()} may still fail due to missing or invalid (combination of) options.
 * This holds especially for {@link MountBuilder#setMountFlags(String)};
 */
public interface MountBuilder {

	/**
	 * Use the given host name as the loopback address.
	 *
	 * @param hostName string conforming with the uri host part
	 * @return <code>this</code>
	 * @throws UnsupportedOperationException If {@link MountCapability#LOOPBACK_HOST_NAME} is not supported
	 */
	@Contract("_ -> this")
	default MountBuilder setLoopbackHostName(String hostName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Use the given TCP port of the loopback address.
	 *
	 * @param port Fixed TCP port or 0 to use a system-assigned port
	 * @return <code>this</code>
	 * @throws UnsupportedOperationException If {@link MountCapability#LOOPBACK_PORT} is not supported
	 */
	@Contract("_ -> this")
	default MountBuilder setLoopbackPort(@Range(from = 0, to = Short.MAX_VALUE) int port) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the mount point.
	 * <p>
	 * Unless the mount provide supports {@link MountCapability#MOUNT_TO_SYSTEM_CHOSEN_PATH}, setting a mount point is required.
	 *
	 * @param mountPoint Where to mount the volume
	 * @return <code>this</code>
	 */
	@Contract("_ -> this")
	default MountBuilder setMountpoint(Path mountPoint) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets mount flags.
	 *
	 * @param mountFlags Mount flags
	 * @return <code>this</code>
	 * @throws UnsupportedOperationException If {@link MountCapability#MOUNT_FLAGS} is not supported
	 * @see MountService#getDefaultMountFlags()
	 */
	@Contract("_ -> this")
	default MountBuilder setMountFlags(String mountFlags) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Instructs the mount to be read-only.
	 *
	 * @param mountReadOnly Whether to mount read-only.
	 * @return <code>this</code>
	 * @throws UnsupportedOperationException If {@link MountCapability#READ_ONLY} is not supported
	 */
	@Contract("_ -> this")
	default MountBuilder setReadOnly(boolean mountReadOnly) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets a unique volume id.
	 *
	 * @param volumeId String conforming with the os-dependent path component restrictions
	 * @return <code>this</code>
	 * @throws UnsupportedOperationException If {@link MountCapability#VOLUME_ID} is not supported
	 */
	@Contract("_ -> this")
	default MountBuilder setVolumeId(String volumeId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Mounts the file system.
	 *
	 * @return A mount handle
	 * @throws MountFailedException If mounting failed
	 */
	@Contract(" -> new")
	Mount mount() throws MountFailedException;

}
