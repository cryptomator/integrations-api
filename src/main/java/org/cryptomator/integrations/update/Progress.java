package org.cryptomator.integrations.update;

public class Progress {
	private final long nOps;
	private final long oP;
	private final long status;
	private final long progress;
	private final String error;
	private final String errorMessage;

	public Progress(long nOps, long oP, long status, long progress, String error, String errorMessage) {
		this.nOps = nOps;
		this.oP = oP;
		this.status = status;
		this.progress = progress;
		this.error = error;
		this.errorMessage = errorMessage;
	}

	public long getOP() { return oP; }
	public long getNOps() {
		return nOps;
	}
	public long getStatus() {
		return status;
	}
	public long getProgress() {
		return progress;
	}
	public String getError() { return error; }
	public String getErrorMessage() { return errorMessage; }
}
