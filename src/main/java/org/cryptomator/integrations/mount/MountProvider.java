package org.cryptomator.integrations.mount;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface MountProvider {

	/**
	 * Loads the best-suited AutoStartProvider.
	 *
	 * @return preferred AutoStartProvider (if any)
	 * @since 1.1.0
	 */
	static Stream<MountProvider> get() {
		return IntegrationsLoader.loadAll(MountProvider.class).filter(MountProvider::isSupported);
	}

	String displayName();

	boolean isSupported();

	MountBuilder forPath(Path vfsRoot);

	default String getDefaultMountPoint() {
		throw new UnsupportedOperationException();
	}

	default String getDefaultMountFlags() {
		throw new UnsupportedOperationException();
	}

	;

	interface MountedVolume extends AutoCloseable {

		//TODO: either this or reveal method, not both
		Path getAccessPoint();

		//TODO: is this needed? why not just let the consumer reveal?
		void reveal(Consumer<Path> cmd);

		void unmout() throws UnmountFailedException;

		default void unmountForced() throws UnmountFailedException {
			throw new UnsupportedOperationException();
		}

		default void close() throws UnmountFailedException {
			unmout();
		}
	}

	//ALL FEATURES
	enum Features {
		CUSTOM_FLAGS,
		MOUNT_POINT_EMPTY_DIR,
		MOUNT_POINT_DRIVE_LETTER,
		MOUNT_POINT_PATH_PREFIX,
		READ_ONLY,
		UNMOUNT_FORCED,
		ON_EXIT_ACTION,
		PORT,
		DEFAULT_MOUNT_POINT,
		DEFAULT_MOUNT_FLAGS
	}

	Set<Features> supportedFeatures();

	/**
	 * Builder to mount a virtual filesystem.
	 * <p>
	 * The setter may validate the input, but no guarantee is given that the final mount option does not fail due to invalid input.
	 * This holds especially for {@link this#setMountFlags(String)};
	 */
	interface MountBuilder {

		//TODO: Idea: every setter verifies the set and can throw an IllegalArgumentException
		default MountBuilder setMountpoint(Path p) {
			throw new UnsupportedOperationException();
		}

		default MountBuilder setOnExitAction(Consumer<Throwable> onExitAction) {
			throw new UnsupportedOperationException();
		}

		default MountBuilder setMountFlags(String mountFlags) {
			throw new UnsupportedOperationException();
		}

		default MountBuilder setReadOnly(boolean mountReadOnly) {
			throw new UnsupportedOperationException();
		}

		MountedVolume mount() throws MountFailedException;

	}


}
