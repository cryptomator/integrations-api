package org.cryptomator.integrations.update;

@FunctionalInterface
public interface ProgressListener {
	void onProgress(Progress progress);
}
