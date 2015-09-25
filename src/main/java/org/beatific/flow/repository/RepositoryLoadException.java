package org.beatific.flow.repository;

public class RepositoryLoadException extends RuntimeException {

	private static final long serialVersionUID = -4450696527730190860L;

	public RepositoryLoadException() {
        super();
    }
	
	public RepositoryLoadException(String s) {
        super(s);
    }
	
	public RepositoryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public RepositoryLoadException(Throwable cause) {
        super(cause);
    }
}
