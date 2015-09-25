package org.beatific.flow.repository;

public class RepositoryTypeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5424429080332018750L;

	public RepositoryTypeNotFoundException() {
        super();
    }
	
	public RepositoryTypeNotFoundException(String s) {
        super(s);
    }
	
	public RepositoryTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public RepositoryTypeNotFoundException(Throwable cause) {
        super(cause);
    }
}
