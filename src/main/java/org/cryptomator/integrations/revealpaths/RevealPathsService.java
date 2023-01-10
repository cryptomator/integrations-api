package org.cryptomator.integrations.revealpaths;

import org.cryptomator.integrations.common.IntegrationService;
import org.cryptomator.integrations.common.IntegrationsLoader;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface RevealPathsService extends IntegrationService {

	/**
	 * Loads all supported service implementations.
	 *
	 * @return Stream of supported RevealPathsService implementations (may be empty)
	 */
	static Stream<RevealPathsService> get() {
		return IntegrationsLoader.loadAll(RevealPathsService.class).filter(RevealPathsService::isSupported);
	}

	/**
	 * Opens the parent of the given path in the system default file manager and highlights the resource the path points to.
	 *
	 * @param p Path to reveal
	 * @throws RevealFailedException             If the file manager could not be opened or {@code p} does not have a parent
	 * @throws java.nio.file.NoSuchFileException If {@code p} does not exist
	 */
	//TODO: Throw IllegalArgumenException if p.getParent() == null?
	void reveal(Path p) throws RevealFailedException, NoSuchFileException;

	/**
	 * Opens the given directory in the system default file manager and highlights all files from the list.
	 *
	 * @param directory
	 * @param childNames
	 * @throws RevealFailedException             If the file manager could not be opened
	 * @throws java.nio.file.NoSuchFileException If {@code directory} does not exist or is not a directory
	 * @throws IllegalArgumentException			 If {@code childNames} contains non-relative paths or paths with a name count != 1 in normalized form
	 * @throws UnsupportedOperationException     If this service implementation does not support revealing multiple files
	 */
	default void reveal(Path directory, List<Path> childNames) throws RevealFailedException, NoSuchFileException {
		throw new UnsupportedOperationException();
	}

}
