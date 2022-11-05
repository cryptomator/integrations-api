package org.cryptomator.integrations.mount;

import java.net.URI;
import java.nio.file.Path;

/**
 * A {@link Mount}'s mount point. There are two types of mount points: Path-based and URI-based.
 */
public sealed interface Mountpoint permits Mountpoint.WithPath, Mountpoint.WithUri {

	/**
	 * Gets an URI representation of this mount point.
	 *
	 * @return an URI pointing to this mount point
	 */
	URI uri();

	static Mountpoint forUri(URI uri) {
		return new WithUri(uri);
	}

	static Mountpoint forPath(Path path) {
		return new WithPath(path);
	}

	record WithUri(URI uri) implements Mountpoint {
	}

	record WithPath(Path path) implements Mountpoint {

		public URI uri() {
			return path.toUri();
		}

	}
}
