package org.cryptomator.integrations.update;

@FunctionalInterface
public interface SpawnStartedListener {
	void onSpawnStarted(SpawnStarted spawnStarted);
}
