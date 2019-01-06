package de.kreth.clubhelperbackend.google.spreadsheet;

public enum SheetService {

	INSTANCE;
	private final transient Sheets service = new SheetImpl();
	
	public Sheets getService() {
		return service;
	}

}
