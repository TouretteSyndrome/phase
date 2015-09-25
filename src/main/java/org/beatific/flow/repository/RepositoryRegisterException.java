package org.beatific.flow.repository;

public class RepositoryRegisterException extends RuntimeException {


	private static final long serialVersionUID = -5782352627334281774L;

	public RepositoryRegisterException() {
        super();
    }
	
	public RepositoryRegisterException(String s) {
        super(s);
    }
	
	public RepositoryRegisterException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public RepositoryRegisterException(Throwable cause) {
        super(cause);
    }
}
