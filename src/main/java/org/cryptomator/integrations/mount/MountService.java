package org.cryptomator.integrations.mount;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A mechanism to mount a file system.
 *
 * @since 1.2.0
 */
public interface MountService {

	/**
	 * Loads all supported mount providers.
	 *
	 * @return Stream of supported MountProviders (may be empty)
	 */
	static Stream<MountService> get() {
		return IntegrationsLoader.loadAll(MountService.class).filter(MountService::isSupported);
	}

	/**
	 * Name of this provider.
	 *
	 * @return A human readable name of this provider
	 */
	String displayName();

	/**
	 * Indicates, if this provider can be used.
	 *
	 * @return true, if this provider is supported in the current OS environment
	 * @implSpec This check needs to return fast and in constant time
	 */
	boolean isSupported();

	/**
	 * Default mount flags. May be empty.
	 *
	 * @param mountName Name of the mount in the OS
	 * @return Concatenated String of valid mount flags
	 * @throws UnsupportedOperationException If {@link MountCapability#MOUNT_FLAGS} is not supported
	 */
	default String getDefaultMountFlags(String mountName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The default TCP port of the loopback address used by this provider.
	 *
	 * @return fixed TCP port or 0 to use a system-assigned port
	 * @throws UnsupportedOperationException If {@link MountCapability#LOOPBACK_PORT} is not supported
	 */
	@Range(from = 0, to = Short.MAX_VALUE)
	default int getDefaultLoopbackPort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Mount features supported by this provider.
	 *
	 * @return Set of supported {@link MountCapability}s
	 */
	Set<MountCapability> supportedFeatures();

	/**
	 * Tests whether this provider supports the given feature.
	 *
	 * @param feature The feature
	 * @return {@code true} if supported
	 */
	default boolean supportsFeature(MountCapability feature) {
		return supportedFeatures().contains(feature);
	}


	/**
	 * Creates a new mount builder.
	 *
	 * @param fileSystemRoot The root of the VFS to be mounted
	 * @return New mount builder
	 */
	@Contract("_ -> new")
	MountBuilder forFileSystem(Path fileSystemRoot);

}
