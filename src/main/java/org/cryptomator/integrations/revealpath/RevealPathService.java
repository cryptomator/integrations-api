package org.cryptomator.integrations.revealpath;

import org.cryptomator.integrations.common.IntegrationsLoader;

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
	 * Reveal the path in the system default file manager.
	 * <p>
	 * If the path points to a file, the parent of the file is openend and file is selected in the file manager window.
	 * If the path points to a directory, the directory is opened and its content shown in the file manager window.
	 *
	 * @param p Path to reveal
	 * @throws RevealFailedException if revealing the path failed
	 */
	void reveal(Path p) throws RevealFailedException;

	/**
	 * Indicates, if this provider can be used.
	 *
	 * @return true, if this provider is supported in the current OS environment
	 * @implSpec This check needs to return fast and in constant time
	 */
	boolean isSupported();

}
