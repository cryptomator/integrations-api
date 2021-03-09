package org.cryptomator.integrations.autolock;

@FunctionalInterface
public interface AutoLockListener {
	void systemStateChanged(SystemState systemState);
}
