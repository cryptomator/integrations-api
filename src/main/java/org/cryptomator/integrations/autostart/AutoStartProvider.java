package org.cryptomator.integrations.autostart;

import org.cryptomator.integrations.common.IntegrationsLoader;
import org.jetbrains.annotations.Blocking;

import java.util.Optional;

public interface AutoStartProvider {

	/**
	 * Loads the best-suited AutoStartProvider.
	 *
	 * @return preferred AutoStartProvider (if any)
	 * @since 1.1.0
	 */
	static Optional<AutoStartProvider> get() {
		return IntegrationsLoader.load(AutoStartProvider.class);
	}

	@Blocking
	void enable() throws ToggleAutoStartFailedException;

	@Blocking
	void disable() throws ToggleAutoStartFailedException;

	boolean isEnabled();

}
