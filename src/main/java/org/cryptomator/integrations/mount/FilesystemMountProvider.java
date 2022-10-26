package org.cryptomator.integrations.mount;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Vorteile:
 * * IoC: Ein Provider weiß was er kann und was nicht!
 * * Wegfall der komplizierten Mountpoint Logik
 * * App beschränkt sich mehr auf GUI und Logik darum
 * * Einfaches ändern/entfernen eines Providers
 * * Mounten ist eine Systemintegration!
 * * Tests!?
 * * Illegal Mount Options Check?
 * * In der GUI sieht der Nutzer sofort, was sich ändert.
 * <p>
 * Nachteile:
 * * Code-Duplikation
 * * WebDAV-ServerPort? (-> Legacy Option in Preferences?)
 * <p>
 * Ungeklärt:
 * * Anzeigen von Fehler dem User (-> Translations nutzbar?)
 * * Langfristiger Wechsel zu eigenständigen Prozessen?
 */
public interface FilesystemMountProvider {

	/**
	 * Loads all supported MountProvider.
	 *
	 * @return Stream of supported MountProviders
	 * @since 1.1.0
	 */
	static Stream<FilesystemMountProvider> get() {
		return IntegrationsLoader.loadAll(FilesystemMountProvider.class).filter(FilesystemMountProvider::isSupported);
	}

	/**
	 * Name of this provider
	 *
	 * @return A human readable name of this provider
	 */
	String displayName();

	/**
	 * Indicates, if this provider can be used.
	 *
	 * @return true, if this provider is supported in the current OS environment
	 */
	boolean isSupported();


	/**
	 * A default mountpoint any filesystem can be mounted to.
	 * Note, that this method:
	 * <ol>
	 *    <li> method is optional. If not implemented, throws an {@link UnsupportedOperationException}</li>
	 *    <li> is not required to serve static content. It should be considerer as a "mount point maker" with the implemetnation as the template engine</li>
	 * </ol>
	 *
	 * @param mountPointSuffix String used in the generation of a mount point.
	 * @return A path to a possible mount point.
	 */
	default Path getDefaultMountPoint(String mountPointSuffix) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Default mount flags.
	 * Implementation required if feature {@link MountFeature#MOUNT_FLAGS} is supported.
	 *
	 * @param mountName Name of the mount in the OS
	 * @return Concatenated String of valid mount flags
	 */
	default String getDefaultMountFlags(String mountName) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Mount features supported by this provider.
	 *
	 * @return Set of supported {@link MountFeature}s
	 */
	Set<MountFeature> supportedFeatures();


	FileSystemMount.Builder forFileSystem(Path fileSystemRoot);

}
