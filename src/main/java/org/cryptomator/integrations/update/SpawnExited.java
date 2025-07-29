package org.cryptomator.integrations.update;

public class SpawnExited {
	private final long pid;
	private final long exitStatus;

	public SpawnExited(long pid, long exitStatus) {
		this.pid = pid;
		this.exitStatus = exitStatus;
	}

	public long getPid() {  return pid; }
	public long getExitStatus() { return exitStatus; }
}
