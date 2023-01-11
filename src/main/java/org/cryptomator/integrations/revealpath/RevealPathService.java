package org.cryptomator.integrations.revealpath;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface RevealPathService {

	/**
	 * Loads all supported service providers.
	 *
	 * @return Stream of supported RevealPathsService implementations (may be empty)
	 */
	static Stream<RevealPathService> get() {
		return IntegrationsLoader.loadAll(RevealPathService.class).filter(RevealPathService::isSupported);
	}

	/**
	 * Opens the parent of the given path in the system default file manager and highlights the resource the path points to.
	 *
	 * @param p Path to reveal
	 * @throws RevealFailedException             If the file manager could not be opened
	 * @throws IllegalArgumentException 		 If {@code p} does not have a parent
	 */
	void reveal(Path p) throws RevealFailedException, NoSuchFileException;

	/**
	 * Indicates, if this provider can be used.
	 *
	 * @return true, if this provider is supported in the current OS environment
	 * @implSpec This check needs to return fast and in constant time
	 */
	boolean isSupported();

}
