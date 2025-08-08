package org.cryptomator.integrations.update;

import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;

@ApiStatus.Experimental
public class UpdateFailedException extends IOException {

	public UpdateFailedException(String message) {
		super(message);
	}

	public UpdateFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
