package org.cryptomator.integrations.sidebar;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Service for integrating a given path into the sidebar/quick access bar of a filemanager.
 */
public interface SidebarService {

	/**
	 * Creates an entry in the filemanager sidebar.
	 *
	 * @param target      The filesystem path the sidebar entry points to
	 * @param displayName The display name of the sidebar entry
	 * @return a {@link SidebarEntry }, used to remove the entry again
	 * @throws SidebarServiceException if adding an entry to the filemanager sidebar fails
	 * @apiNote It depends on the service implementation wether the display name is used or not.
	 */
	@Blocking
	SidebarEntry add(@NotNull Path target, @NotNull String displayName) throws SidebarServiceException;

	/**
	 * An entry of the filemanager sidebar, created by an implementation of this service.
	 */
	interface SidebarEntry {

		/**
		 * Removes this entry from the sidebar.
		 *
		 * @throws SidebarServiceException if removal fails.
		 * @implSpec ServiceProviders should make this function <em>idempotent</em>, i.e. after the method is called once and succeeded, consecutive calls should not change anything or throw an error.
		 */
		@Blocking
		void remove() throws SidebarServiceException;

	}

	static Optional<SidebarService> get() {
		return IntegrationsLoader.load(SidebarService.class);
	}
}
