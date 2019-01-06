package de.kreth.clubhelperbackend.google.spreadsheet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;

import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;

public interface Sheets {

	ValueRange getRange(String sheetTitle, String range) throws IOException;

	JumpHeightSheet changeTitle(ServletRequest request, Sheet sheet, String name) throws IOException, InterruptedException;

	ExtendedValue set(String sheetTitle, int column, int row, String value) throws IOException;

	ExtendedValue set(String sheetTitle, int column, int row, double value) throws IOException;

	void delete(JumpHeightSheet test) throws IOException;

	JumpHeightSheet create(ServletRequest request, String title) throws IOException;

	List<JumpHeightSheet> getSheets(ServletRequest request) throws IOException, InterruptedException;

	JumpHeightSheet get(ServletRequest request, String title) throws IOException, InterruptedException;

}
