package org.cryptomator.integrations.mount;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

// Vorteile:
//  * IoC: Ein Provider weiß was er kann und was nicht!
//  * Wegfall der komplizierten Mountpoint Logik
//  * App beschränkt sich mehr auf GUI und Logik darum
//  * Einfaches ändern/entfernen eines Providers
//  * Mounten ist eine Systemintegration!
//  * Tests!?
//  * Illegal Mount Options Check?
//  * In der GUI sieht der Nutzer sofort, was sich ändert.
// Nachteile:
//  * Code-Duplikation
//  * WebDAV-ServerPort? (-> Legacy Option in Preferences?)
// Ungeklärt:
//  * Anzeigen von Fehler dem User (-> Translations nutzbar?)
//  * Langfristiger Wechsel zu eigenständigen Prozessen?

/**
 * A mechanism to mount a file system.
 *
 * @since 1.2.0
 */
public interface MountProvider {

	/**
	 * Loads all supported mount providers.
	 *
	 * @return Stream of supported MountProviders (may be empty)
	 */
	static Stream<MountProvider> get() {
		return IntegrationsLoader.loadAll(MountProvider.class).filter(MountProvider::isSupported);
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
	 * A suitable mount point suggested by this provider.
	 * <p>
	 * Other than caller-provided mount points, the mount point suggested by this method can be
	 * passed to {@link MountBuilder#setMountpoint(Path)} without further ado.
	 *
	 * @param mountPointSuffix String used in the generation of a mount point.
	 * @return A path to a possible mount point.
	 * @throws UnsupportedOperationException If {@link MountFeature#DEFAULT_MOUNT_POINT} is not supported
	 */
	default Path getDefaultMountPoint(String mountPointSuffix) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Default mount flags. May be empty.
	 *
	 * @param mountName Name of the mount in the OS
	 * @return Concatenated String of valid mount flags
	 * @throws UnsupportedOperationException If {@link MountFeature#MOUNT_FLAGS} is not supported
	 */
	default String getDefaultMountFlags(String mountName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The default TCP port used by this provider.
	 *
	 * @return fixed TCP port or 0 to use a system-assigned port
	 * @throws UnsupportedOperationException If {@link MountFeature#PORT} is not supported
	 */
	@Range(from = 0, to = Short.MAX_VALUE)
	default int getDefaultPort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Mount features supported by this provider.
	 *
	 * @return Set of supported {@link MountFeature}s
	 */
	Set<MountFeature> supportedFeatures();


	/**
	 * Creates a new mount builder.
	 *
	 * @param fileSystemRoot The root of the VFS to be mounted
	 * @return New mount builder
	 */
	@Contract("_ -> new")
	MountBuilder forFileSystem(Path fileSystemRoot);

}
