package de.kreth.clubhelperbackend.google.spreadsheet;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.ValueRange;

public class SheetImpl implements Sheets {

	private static final List<Sheet> sheets = new ArrayList<>();
	private Logger log = LoggerFactory.getLogger(getClass());
	private final GoogleSpreadsheetsAdapter service;
	
	public SheetImpl() {
		if(log.isInfoEnabled()) {
			log.info(GoogleSpreadsheetsAdapter.class.getName() + " not initiated, creating...");
		}
		try {
			service = new GoogleSpreadsheetsAdapter();
		} catch (IOException | GeneralSecurityException e) {
			log.error("unable to init " + getClass().getName() + ", Service won't work.", e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public JumpHeightSheet get(ServletRequest request, String title) throws IOException, InterruptedException {
		if(log.isDebugEnabled()) {
			log.debug("Getting " + Sheet.class.getName() + " for " + title);
		}
		Sheet result = getForName(request, title);
		try {
			return new JumpHeightSheet(result);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public List<JumpHeightSheet> getSheets(ServletRequest request) throws IOException, InterruptedException {
		List<JumpHeightSheet> result = new ArrayList<>();
		for (Sheet s: getAllSheets(request)) {
			try {
				result.add(new JumpHeightSheet(s));
			} catch (SheetDataException e) {
				log.error("unable to add sheet: " + s, e);
			}
		}
		return result;
	}
	
	private Sheet getForName(ServletRequest request, String title) throws IOException, InterruptedException {
		List<Sheet> all = getAllSheets(request);

		for (Sheet s: all) {
			if(log.isTraceEnabled()) {
				log.trace("found Sheet: " + s.getProperties().getTitle());
			}
			if(s.getProperties().getTitle().equals(title)) {
				if(log.isTraceEnabled()) {
					log.trace("returning Sheet: " + s);
				}
				return s;
			}
		}
		throw new IOException("Sheet with title \"" + title + "\" not found.");
	}

	private List<Sheet> getAllSheets(ServletRequest request) throws IOException, InterruptedException {
		if(sheets.isEmpty() == false){
			return sheets;
		}
		sheets.addAll(service.getSheets(request));
		return sheets;
	}

	@Override
	public JumpHeightSheet create(ServletRequest request, String title) throws IOException {
		try {
			Sheet dublicateTo = service.dublicateTo(request, "Vorlage", title);
			sheets.add(dublicateTo);
			return new JumpHeightSheet(dublicateTo);
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}

	@Override
	public void delete(JumpHeightSheet test) throws IOException {
		sheets.remove(test.sheet);
		service.delete(test.sheet);
	}

	@Override
	public ExtendedValue set(String sheetTitle, int column, int row, double value) throws IOException {
		ValueRange content = new ValueRange();
		content = content.setValues(Arrays.asList(Arrays.asList(value)));
		service.setValue(sheetTitle, column, row, content );
		ExtendedValue res = new ExtendedValue();
		res.setNumberValue(value);
		return res;
	}
	
	@Override
	public ExtendedValue set(String sheetTitle, int column, int row, String value) throws IOException {
		ValueRange content = new ValueRange();
		content = content.setValues(Arrays.asList(Arrays.asList(value)));
		service.setValue(sheetTitle, column, row, content );
		ExtendedValue res = new ExtendedValue();
		res.setStringValue(value);
		return res;
	}

	@Override
	public JumpHeightSheet changeTitle(ServletRequest request, Sheet sheet, String name) throws IOException, InterruptedException {
		service.setSheetTitle(sheet, name);
		sheets.clear();
		return get(request, name);
	}

	@Override
	public ValueRange getRange(String sheetTitle, String range) throws IOException {
		return service.getValues(sheetTitle, range);
	}

}
