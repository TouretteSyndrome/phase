package org.beatific.flow.flow;

public class ClassNotMatchException extends RuntimeException {


	private static final long serialVersionUID = 3109966360591836096L;

	public ClassNotMatchException() {
		super();
	}

	public ClassNotMatchException(String s) {
		super(s);
	}

	public ClassNotMatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClassNotMatchException(Throwable cause) {
		super(cause);
	}
}
