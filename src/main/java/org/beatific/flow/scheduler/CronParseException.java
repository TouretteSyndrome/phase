package org.beatific.flow.scheduler;

public class CronParseException extends RuntimeException {

	private static final long serialVersionUID = -4450696527730190860L;

	public CronParseException() {
		super();
	}

	public CronParseException(String s) {
		super(s);
	}

	public CronParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public CronParseException(Throwable cause) {
		super(cause);
	}
}