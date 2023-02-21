package org.cryptomator.integrations.mount;

import java.io.IOException;

/**
 * Handle to control the lifecycle of a mounted file system.
 * <p>
 * Created by {@link MountBuilder}
 */
public interface Mount extends AutoCloseable {

	/**
	 * Returns the absolute OS path, where this mount can be accessed.
	 *
	 * @return Absolute path to the mountpoint.
	 */
	Mountpoint getMountpoint();

	/**
	 * Unmounts the mounted Volume.
	 * <p>
	 * If possible, attempt a graceful unmount.
	 *
	 * @throws UnmountFailedException If the unmount was not successful.
	 * @see #unmountForced()
	 */
	void unmount() throws UnmountFailedException;

	/**
	 * If supported, force-unmount the volume.
	 *
	 * @throws UnmountFailedException        If the unmount was not successful.
	 * @throws UnsupportedOperationException If {@link MountCapability#UNMOUNT_FORCED} is not supported
	 */
	default void unmountForced() throws UnmountFailedException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unmounts (if required) and releases any resources.
	 *
	 * @throws UnmountFailedException Thrown if unmounting failed
	 * @throws IOException            Thrown if cleaning up resources failed
	 */
	default void close() throws UnmountFailedException, IOException {
		unmount();
	}


}
