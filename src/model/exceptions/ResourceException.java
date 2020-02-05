package model.exceptions;

public class ResourceException extends Exception{

	private static final long serialVersionUID = 1L;

	public ResourceException() {
		super();
	}

    public ResourceException(String message) {
    	super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ResourceException(Throwable cause) {
        super(cause);
    }
}
