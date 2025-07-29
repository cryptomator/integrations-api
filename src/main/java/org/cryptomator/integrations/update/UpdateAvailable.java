package org.cryptomator.integrations.update;

public class UpdateAvailable {
	private final String runningCommit;
	private final String localCommit;
	private final String remoteCommit;

	public UpdateAvailable(String runningCommit, String localCommit, String remoteCommit) {
		this.runningCommit = runningCommit;
		this.localCommit = localCommit;
		this.remoteCommit = remoteCommit;
	}

	public String getRunningCommit() {
		return runningCommit;
	}
	public String getLocalCommit() {
		return localCommit;
	}
	public String getRemoteCommit() {
		return remoteCommit;
	}
}
