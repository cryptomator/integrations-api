package org.cryptomator.integrations.autostart;

import org.cryptomator.integrations.common.IntegrationsLoader;

import java.util.Optional;

public interface AutoStartProvider {

	static Optional<AutoStartProvider> get() {
		return IntegrationsLoader.load(AutoStartProvider.class);
	}

	void enable() throws ToggleAutoStartFailedException;

	void disable() throws ToggleAutoStartFailedException;

	boolean isEnabled();

}
