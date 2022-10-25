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

	interface MountedVolume extends AutoCloseable {

		//TODO: either this or reveal method, not both
		Path getAccessPoint();

		//TODO: is this needed? why not just let the consumer reveal?
		void reveal(Consumer<Path> cmd);

		void unmout() throws UnmountFailedException;

		default void close() throws UnmountFailedException {
			unmout();
		}
	}

	//ALL FEATURES
	enum Features {
		CUSTOM_FLAGS,
		MOUNT_POINT_EMPTY_DIR,
		MOUNT_POINT_DRIVE_LETTER,
		READ_ONLY,
		FORCED_UNMOUNT,
		ON_EXIT_ACTION,
		PORT
	}

	Set<Features> supportedFeatures();

	//TODO: if not supported No-op or throw?
	// no-op better for unified code (just load off all shiat into the builder and see what the result is)
	// does not hold for invalid input
	interface MountBuilder {

		MountBuilder setMountpoint(Path p); //TODO: Idea: every setter verifies the set and can throw an IllegalArgumentException

		MountBuilder setOnExitAction(Consumer<Throwable> onExitAction);

		MountBuilder setCustomFlags(String customFlags);

		MountBuilder setReadOnly(boolean mountReadOnly);

		MountedVolume mount() throws MountFailedException;

	}


}
