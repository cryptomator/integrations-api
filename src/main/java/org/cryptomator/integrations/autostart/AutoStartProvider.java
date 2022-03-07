package org.cryptomator.integrations.autostart;

import org.cryptomator.integrations.common.IntegrationsLoader;

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

	void enable() throws ToggleAutoStartFailedException;

	void disable() throws ToggleAutoStartFailedException;

	boolean isEnabled();

}
