package org.cryptomator.integrations.autostart;

public interface AutoStartProvider {

	void enable() throws ToogleAutoStartFailedException;

	void disable() throws ToogleAutoStartFailedException;

	boolean isEnabled();

}
