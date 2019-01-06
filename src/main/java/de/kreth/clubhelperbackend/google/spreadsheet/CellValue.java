package de.kreth.clubhelperbackend.google.spreadsheet;

import java.text.DateFormat;
import java.util.Date;

public class CellValue<T> {
	
	private T innerObject;
	private int column;
	private int row;
	
	public CellValue(T object, int columnIndex, int rowIndex) {
		super();
		assert (object != null);
		this.innerObject = object;
		this.column = columnIndex;
		this.row = rowIndex;
	}

	public final T getObject() {
		return innerObject;
	}

	public final int getColumn() {
		return column;
	}

	public final int getRow() {
		return row;
	}

	@Override
	public final String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CellValue ");
		stringBuilder.append(GoogleSpreadsheetsAdapter.intToColumn(column));
		stringBuilder.append(row);
		stringBuilder.append("=");
		if (innerObject instanceof Date) {
			stringBuilder.append(DateFormat.getDateTimeInstance().format(innerObject));
		} else {
			stringBuilder.append(innerObject);
		}
		
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + ((innerObject == null) ? 0 : innerObject.hashCode());
		result = prime * result + row;
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		CellValue<T> other = (CellValue<T>) obj;
		if (column != other.column) {
			return false;
		}
		if (innerObject == null) {
			if (other.innerObject != null) {
				return false;
			}
		} else if (!innerObject.equals(other.innerObject)) {
			return false;
		}
		if (row != other.row) {
			return false;
		}
		return true;
	}
	
}
