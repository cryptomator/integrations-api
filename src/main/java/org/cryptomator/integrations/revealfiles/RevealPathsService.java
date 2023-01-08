package org.cryptomator.integrations.revealfiles;

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
	 * @throws RevealFailedException             If the file manager could not be opened or {@code p} does not have a parent //TODO: or throw IllegalArgumenException
	 * @throws java.nio.file.NoSuchFileException If {@code p} does not exist
	 */
	void reveal(Path p) throws RevealFailedException, NoSuchFileException;

	/**
	 * Opens the given directory in the system default file manager and highlights all files from the list.
	 *
	 * @throws RevealFailedException             If the file manager could not be opened
	 * @throws java.nio.file.NoSuchFileException If {@code directory} does not exist or is not a directory
	 * @throws RuntimeException                  If at least one file from the list cannot be found? TODO: this might be unncessary/not possible
	 * @throws UnsupportedOperationException     If this service implementation does not support revealing multiple files
	 */
	default void reveal(Path directory, List<String> childNames) throws RevealFailedException, NoSuchFileException {
		throw new UnsupportedOperationException();
	}
}
