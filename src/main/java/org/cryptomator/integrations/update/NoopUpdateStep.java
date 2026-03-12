package org.cryptomator.integrations.update;

import java.util.concurrent.TimeUnit;

record NoopUpdateStep(String description) implements UpdateStep {

	@Override
	public void start() {}

	@Override
	public double preparationProgress() {
		return -1.0;
	}

	@Override
	public void cancel() {}

	@Override
	public void await() {}

	@Override
	public boolean await(long timeout, TimeUnit unit) {
		return true; // always done
	}

	@Override
	public UpdateStep nextStep() {
		return null;
	}
}
