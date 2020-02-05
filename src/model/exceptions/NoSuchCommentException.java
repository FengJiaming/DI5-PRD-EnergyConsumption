package model.exceptions;

public class NoSuchCommentException extends Exception{
	
	private static final long serialVersionUID = -1257647143189079034L;

	public NoSuchCommentException(){
		super();
	}

	public NoSuchCommentException(String message) {
		super(message);
	}
}
