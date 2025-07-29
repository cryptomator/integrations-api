package org.cryptomator.integrations.update;

public class SpawnStarted {
	private final long pid;
	private final long relPid;

	public SpawnStarted(long pid, long relPid) {
		this.pid = pid;
		this.relPid = relPid;
	}

	public long getPid() {  return pid; }
	public long getRelPid() { return relPid; }
}
