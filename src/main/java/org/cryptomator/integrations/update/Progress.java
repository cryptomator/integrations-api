package org.cryptomator.integrations.update;

public class Progress {
	private final long status;
	private final long progress;

	public Progress(long status, long progress) {
		this.status = status;
		this.progress = progress;
	}

	public long getStatus() {
		return status;
	}

	public long getProgress() {
		return progress;
	}
}
