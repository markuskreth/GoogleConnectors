package de.kreth.clubhelperbackend.google.spreadsheet;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets;

public class DiscoverytestsSpreadsheed {

	@Test
	public void test() {

		Sheets.Builder builder = new Sheets.Builder(
				new com.google.api.client.extensions.appengine.http.UrlFetchTransport(), 
				new com.google.api.client.json.gson.GsonFactory(), null);
		Spreadsheets sheets = builder.build().spreadsheets();
		assertNotNull(sheets);
	}

}
