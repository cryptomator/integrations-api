package org.cryptomator.integrations.mount;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * A filesystem mounted into the OS.
 *
 * Created by {@link FileSystemMount.Builder}
 */
public interface FileSystemMount extends AutoCloseable {

	//TODO: either this or reveal method, not both
	Path getAccessPoint();

	//TODO: is this needed? why not just let the consumer reveal?
	void reveal(Consumer<Path> cmd);

	/**
	 * Unmounts the mounted Volume.
	 * <p>
	 * The unmount procedure should be as gracefule as possible.
	 * If the volume supports a forceful unmount, see {@link FileSystemMount#unmountForced()} can be used.
	 * The most harsh unmount happens by using {@link FileSystemMount#close()}.
	 *
	 * @throws UnmountFailedException If the unmount was not successful.
	 */
	void unmout() throws UnmountFailedException;

	default void unmountForced() throws UnmountFailedException {
		throw new UnsupportedOperationException();
	}

	default void close() throws UnmountFailedException {
		unmout();
	}



	/**
	 * Builder to mount a filesystem.
	 * <p>
	 * The setter may validate the input, but no guarantee is given that the final mount option does not fail due to invalid input.
	 * This holds especially for {@link Builder#setMountFlags(String)};
	 */
	interface Builder {

		//TODO: Idea: every setter verifies the set and can throw an IllegalArgumentException
		default Builder setMountpoint(Path p) {
			throw new UnsupportedOperationException();
		}

		default Builder setOnExitAction(Consumer<Throwable> onExitAction) {
			throw new UnsupportedOperationException();
		}

		default Builder setMountFlags(String mountFlags) {
			throw new UnsupportedOperationException();
		}

		default Builder setReadOnly(boolean mountReadOnly) {
			throw new UnsupportedOperationException();
		}

		FileSystemMount mount() throws MountFailedException;

	}
}
