package org.cryptomator.integrations.update;

public class UpdateAvailable {
	private final String remoteCommit;

	public UpdateAvailable(String remoteCommit) {
		this.remoteCommit = remoteCommit;
	}

	public String getRemoteCommit() {
		return remoteCommit;
	}
}
