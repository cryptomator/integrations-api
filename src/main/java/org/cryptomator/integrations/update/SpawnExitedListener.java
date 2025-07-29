package org.cryptomator.integrations.update;

@FunctionalInterface
public interface SpawnExitedListener {
	void onSpawnExited(SpawnExited spawnExited);
}
