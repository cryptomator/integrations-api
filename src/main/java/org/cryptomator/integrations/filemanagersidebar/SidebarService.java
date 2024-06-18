package org.cryptomator.integrations.filemanagersidebar;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Service for integrating a given path into the sidebar/quick access bar of a filemanager.
 */
public interface SidebarService {

	/**
	 * Creates an entry in the filemanager sidebar.
	 *
	 * @param displayName The display name of the sidebar entry
	 * @param target      The filesystem path the sidebar entry points to.
	 * @return a @{link SidebarEntry } object
	 */
	SidebarEntry add(@NotNull String displayName, @NotNull Path target) throws SidebarServiceException;

	/**
	 * An entry of the filemanager sidebar, created with this service.
	 */
	interface SidebarEntry extends Closeable {

		/**
		 * Removes this entry from the sidebar. Once removed, this object cannot be added again.
		 */
		void remove() throws SidebarServiceException;

		default void close() {
			remove();
		}
	}

	static Optional<SidebarService> get() {
		return IntegrationsLoader.load(SidebarService.class);
	}
}
