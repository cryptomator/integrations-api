package org.cryptomator.integrations.update;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public abstract class UpdateStepAdapter implements Callable<UpdateStep>, Runnable, UpdateStep {

	protected final Thread thread;
	protected volatile UpdateStep result;
	protected volatile Exception exception;

	public UpdateStepAdapter() {
		this.thread = Thread.ofVirtual().name("UpdateStep", 0).unstarted(this);
	}

	@Override
	public final void run() {
		try {
			this.result = this.call();
		} catch (Exception e) {
			this.exception = e;
		}
	}

	@Override
	public void start() throws IllegalThreadStateException {
		thread.start();
	}

	@Override
	public double preparationProgress() {
		return -1.0;
	}

	@Override
	public void cancel() {
		thread.interrupt();
	}

	@Override
	public void await() throws InterruptedException {
		thread.join();
	}

	@Override
	public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
		return thread.join(Duration.of(timeout, unit.toChronoUnit()));
	}

	@Override
	public @Nullable UpdateStep nextStep() throws IllegalStateException, IOException {
		if (!isDone()) {
			throw new IllegalStateException("Update step not completed yet");
		}
		return switch (exception) {
			case null -> result;
			case IOException e -> throw e;
			default -> throw new IOException("Update step failed", exception);
		};
	}
}
