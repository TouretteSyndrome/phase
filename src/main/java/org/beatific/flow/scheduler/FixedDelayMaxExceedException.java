package org.beatific.flow.scheduler;

public class FixedDelayMaxExceedException extends RuntimeException {

	private static final long serialVersionUID = 502427750770054651L;

	public FixedDelayMaxExceedException() {
		super();
	}

	public FixedDelayMaxExceedException(String s) {
		super(s);
	}

	public FixedDelayMaxExceedException(String message, Throwable cause) {
		super(message, cause);
	}

	public FixedDelayMaxExceedException(Throwable cause) {
		super(cause);
	}
}