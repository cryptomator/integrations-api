package org.cryptomator.integrations.update;

@FunctionalInterface
public interface UpdateAvailableListener {
	void onUpdateAvailable(UpdateAvailable updateAvailable);
}
