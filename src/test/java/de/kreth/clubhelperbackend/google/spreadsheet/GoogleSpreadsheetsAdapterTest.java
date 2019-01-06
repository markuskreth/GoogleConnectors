package de.kreth.clubhelperbackend.google.spreadsheet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GoogleSpreadsheetsAdapterTest {

	
	@Test
	public void testSingleChars() {
		assertEquals("A", GoogleSpreadsheetsAdapter.intToColumn(1));
		assertEquals("Z", GoogleSpreadsheetsAdapter.intToColumn(26));
	}

	@Test
	public void testTwoChars() {
		assertEquals("AA", GoogleSpreadsheetsAdapter.intToColumn(27));
		assertEquals("AE", GoogleSpreadsheetsAdapter.intToColumn(31));
		assertEquals("AZ", GoogleSpreadsheetsAdapter.intToColumn(52));
		assertEquals("BA", GoogleSpreadsheetsAdapter.intToColumn(53));
	}
}
