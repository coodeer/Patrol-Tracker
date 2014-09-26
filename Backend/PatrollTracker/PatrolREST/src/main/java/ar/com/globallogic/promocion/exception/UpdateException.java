package ar.com.globallogic.promocion.exception;

public class UpdateException extends RuntimeException{

	private static final long serialVersionUID = 3101209469847855077L;

	private int errorCode;
	
	public UpdateException(){
		super();
	}

	public UpdateException(Throwable e){
		super(e);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
