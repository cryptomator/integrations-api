package org.cryptomator.integrations.quickaccess;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Service adding a system path link to a quick access area of the OS or an application (e.g. file manager).
 *
 * @apiNote On purpose this service does not define, what an "link to a quick access area" is. The defintion depends on the OS. For example, the quick access area can be the home screen/desktop and the link would be an icon leading to the linked path.
 */
public interface QuickAccessService {

	/**
	 * Creates an entry in the quick access area.
	 *
	 * @param target      The filesystem path the quick access entry points to
	 * @param displayName The display name of the quick access entry
	 * @return a {@link QuickAccessEntry }, used to remove the entry again
	 * @throws QuickAccessServiceException if adding an entry to the quick access area fails
	 * @apiNote It depends on the service implementation wether the display name is used or not.
	 */
	@Blocking
	QuickAccessEntry add(@NotNull Path target, @NotNull String displayName) throws QuickAccessServiceException;

	/**
	 * An entry of the quick access area, created by a service implementation.
	 */
	interface QuickAccessEntry {

		/**
		 * Removes this entry from the quick access area.
		 *
		 * @throws QuickAccessServiceException if removal fails.
		 * @implSpec Service implementations should make this function <em>idempotent</em>, i.e. after the method is called once and succeeded, consecutive calls should not change anything or throw an error.
		 */
		@Blocking
		void remove() throws QuickAccessServiceException;

	}

	/**
	 * Loads all supported service providers.
	 *
	 * @return Stream of supported {@link QuickAccessService} implementations (may be empty)
	 */
	static Stream<QuickAccessService> get() {
		return IntegrationsLoader.loadAll(QuickAccessService.class);
	}
}
