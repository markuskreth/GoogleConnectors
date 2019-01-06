package de.kreth.clubhelperbackend.google.spreadsheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CellRange {

	private List<List<String>> values;
	
	private CellRange(Builder builder) {
		this.values = Collections.unmodifiableList(builder.values);
	}

	public List<List<String>> getValues() {
		return values;
	}
	
	public String getValue(int column, int row) {
		return values.get(row).get(column);
	}
	
	@Override
	public String toString() {
		StringBuilder bld = new StringBuilder();
		for (List<String> l: values) {
			if (bld.length() > 0) {
				bld.append("\n");
			}
			bld.append(String.join(", ", l));
		}
		return bld.toString();
	}
	
	public static class Builder {

		private List<List<String>> values = new ArrayList<>();
		
		/**
		 * Add value to Range.
		 * @param columnIndex Column
		 * @param rowIndex	Row
		 * @param value	additional value
		 * @return this
		 */
		public final Builder add(int columnIndex, int rowIndex, String value) {
			List<String> row;
			if (rowIndex >= values.size()) {
				row = new ArrayList<>();
				while (rowIndex > values.size()) {
					values.add(Collections.emptyList());
				}
				values.add(rowIndex, row);
			} else {
				row = values.get(rowIndex);
			}
			row.add(columnIndex, value);
			return this;
		}
		
		public final CellRange build() {
			return new CellRange(this);
		}
	}
}
