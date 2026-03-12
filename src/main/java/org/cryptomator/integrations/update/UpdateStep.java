package org.cryptomator.integrations.update;

import org.cryptomator.integrations.Localization;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@ApiStatus.Experimental
public interface UpdateStep {

	/**
	 * A magic constant indicating that the application shall terminate.
	 * <p>
	 * This step can be returned as the last step of the update process, usually immediately after a restart has been scheduled.
	 */
	UpdateStep EXIT = new NoopUpdateStep(Localization.get().getString("org.cryptomator.api.update.updateStep.EXIT"));

	/**
	 * A magic constant indicating that the update process shall be retried.
	 */
	UpdateStep RETRY = new NoopUpdateStep(Localization.get().getString("org.cryptomator.api.update.updateStep.RETRY"));


	static UpdateStep of(String name, Callable<UpdateStep> nextStep) {
		return new UpdateStepAdapter() {

			@Override
			public UpdateStep call() throws Exception {
				return nextStep.call();
			}

			@Override
			public String description() {
				return name;
			}
		};
	}

	/**
	 * A short description of this update step.
	 * @return a human-readable description of this update step.
	 */
	String description();

	/**
	 * Starts work on this update step in a non-blocking manner.
	 * @throws IllegalThreadStateException if this step has already been started.
	 */
	@NonBlocking
	void start() throws IllegalThreadStateException;

	/**
	 * A thread-safe method to check the progress of the update preparation.
	 * @return a value between 0.0 and 1.0 indicating the progress of the update preparation or -1.0 indicating indeterminate progress.
	 */
	double preparationProgress();

	/**
	 * Cancels this update step and cleans up any temporary resources.
	 */
	void cancel();

	/**
	 * Blocks the current thread until this update step completed or an error occurred.
	 * If this step failed, an exception will be rethrown as soon as attempting to invoke {@link #nextStep()}.
	 * <p>
	 * If the step is already complete, this method returns immediately.
	 *
	 * @throws InterruptedException if the current thread is interrupted while waiting.
	 */
	void await() throws InterruptedException;

	/**
	 * Blocks the current thread until this update step completed or an error occurred, or until the specified timeout expires.
	 * If this step failed, an exception will be rethrown as soon as attempting to invoke {@link #nextStep()}.
	 * <p>
	 * If the step is already complete, this method returns immediately.
	 *
	 * @param timeout the maximum time to wait
	 * @param unit the time unit of the {@code timeout} argument
	 * @return true if the update is prepared
	 */
	boolean await(long timeout, TimeUnit unit) throws InterruptedException;

	default boolean isDone() {
		try {
			return await(0, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}

	/**
	 * After running this step to completion, this method returns the next step of the update process.
	 *
	 * @return the next {@link UpdateStep step} of the update process or <code>null</code> if this was the final step.
	 * @throws IllegalStateException if this step didn't complete yet or other preconditions aren't met.
	 * @throws IOException indicating an error before reaching the next step, e.g. during execution of this step.
	 * @implSpec The returned {@link UpdateStep} must either be stateless or a new instance must be returned on each call.
	 */
	@Nullable
	UpdateStep nextStep() throws IllegalStateException, IOException;

}