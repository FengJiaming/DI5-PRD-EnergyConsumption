package model.exceptions;

public class ModuleException extends Exception {

	private static final long serialVersionUID = -3388688857266748587L;
	
	public ModuleException() {
		super();
	}

    public ModuleException(String message) {
    	super(message);
    }

    public ModuleException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ModuleException(Throwable cause) {
        super(cause);
    }
}
