package org.cryptomator.integrations.mount;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Handle to control the lifecycle of a mounted file system.
 * <p>
 * Created by {@link MountBuilder}
 */
public interface Mount extends AutoCloseable {

	//TODO: either this or reveal method, not both
	Path getAccessPoint();

	//TODO: is this needed? why not just let the consumer reveal?
	// See WebDAV: LinuxGioMounter and LinuxGvfsMounter -> no path, just a command
	void reveal(Consumer<Path> cmd);

	/**
	 * Unmounts the mounted Volume.
	 * <p>
	 * If possible, attempt a graceful unmount.
	 *
	 * @throws UnmountFailedException If the unmount was not successful.
	 * @see #unmountForced()
	 */
	void unmout() throws UnmountFailedException;

	/**
	 * If supported, force-unmount the volume.
	 *
	 * @throws UnmountFailedException        If the unmount was not successful.
	 * @throws UnsupportedOperationException If {@link MountFeature#UNMOUNT_FORCED} is not supported
	 */
	default void unmountForced() throws UnmountFailedException {
		throw new UnsupportedOperationException();
	}

	default void close() throws UnmountFailedException {
		unmout();
	}


}
