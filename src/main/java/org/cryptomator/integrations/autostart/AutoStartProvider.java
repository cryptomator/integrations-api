package org.cryptomator.integrations.autostart;

public interface AutoStartProvider {

	void enable() throws ToggleAutoStartFailedException;

	void disable() throws ToggleAutoStartFailedException;

	boolean isEnabled();

}
