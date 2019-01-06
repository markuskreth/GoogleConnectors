package de.kreth.clubhelperbackend.google.spreadsheet;

public class SheetDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1485583653041856748L;

	public SheetDataException() {
		super();
	}

	public SheetDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public SheetDataException(String message) {
		super(message);
	}

}
