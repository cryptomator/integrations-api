package org.cryptomator.integrations.autolock;

public interface AutoLockProvider {

	/**
	 * Registers a listener that gets notified when the operating system changes its power state / enables the lock screen or screen saver.
	 *
	 * @param listener The listener
	 * @throws AutoLockException If registering the listener failed.
	 */
	void addListener(AutoLockListener listener) throws AutoLockException;

	/**
	 * Removes a previously registered listener.
	 *
	 * @param listener The listener
	 * @throws AutoLockException If removing the listener failed.
	 */
	void removeListener(AutoLockListener listener) throws AutoLockException;

}
