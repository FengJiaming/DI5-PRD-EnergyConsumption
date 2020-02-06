package model.resources.computing.extensions;

public class ExtensionException extends Exception {

	private static final long serialVersionUID = -3388688857266748587L;
	
	public ExtensionException() {
		super();
	}

    public ExtensionException(String message) {
    	super(message);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ExtensionException(Throwable cause) {
        super(cause);
    }
}
