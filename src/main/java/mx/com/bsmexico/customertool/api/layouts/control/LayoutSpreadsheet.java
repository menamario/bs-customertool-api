package mx.com.bsmexico.customertool.api.layouts.control;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutMetaModel;

/**
 * @author jchr
 *
 * @param <T>
 */
public abstract class LayoutSpreadsheet<T> extends SpreadsheetView {

	protected int rowCount = 50;
	protected int columnCount;
	private LayoutMetaModel<T> metamodel;

	public LayoutSpreadsheet(Class<T> type) throws IllegalArgumentException {
		if (type == null) {
			throw new IllegalArgumentException("Type can not be null");
		}
		this.metamodel = new LayoutMetaModel<>(type);
		this.columnCount = metamodel.getFieldCount();
		init(new GridBase(rowCount, columnCount));
		// getStylesheets().add(this.getClass().getResource("spreadsheet.css").toExternalForm());
	}

	/**
	 * @param data
	 * @param row
	 * @return
	 */
	protected abstract ObservableList<SpreadsheetCell> getRow(T data, int row);
	/**
	 * @return
	 */
	protected abstract String [] getFieldOrder();

	
	/**
	 * @param data
	 */
	protected void polulate(List<T> data) {
		// 
		final ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
		SpreadsheetCell cell = null;
		ObservableList<SpreadsheetCell> row = null;
		int rws = 1;
		int dataSize = data.size();
		this.rowCount = dataSize * 2;
		while (rws < this.rowCount) {
			if (rws <= dataSize) {
				this.getGrid().getRows().add(rws, getRow(data.get(rws - 1), rws));
			} else {
				int column = 0;
				row = FXCollections.observableArrayList();
				while (column < this.columnCount) {					
					cell = SpreadsheetCellType.STRING.createCell(rws, column++, 1, 1, StringUtils.EMPTY);					
					row.add(cell);
				}
				setRowEditable(row, true);
				this.getGrid().getRows().add(rws, row);
			}
			rws++;
		}

	}

	/**
	 * 
	 */
	private void init(final GridBase grid) {
		final ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
		rows.add(this.generateHeaderCells());
		rows.addAll(getInitRows());
		grid.setRows(rows);
		setGrid(grid);
	}

	/**
	 * @return
	 */
	private ObservableList<ObservableList<SpreadsheetCell>> getInitRows() {
		final ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
		SpreadsheetCell cell = null;
		ObservableList<SpreadsheetCell> data = null;
		int rws = 0;
		while (rws < this.rowCount) {
			data = FXCollections.observableArrayList();
			int column = 0;
			while (column < this.columnCount) {
				cell = SpreadsheetCellType.STRING.createCell(rws, column++, 1, 1, StringUtils.EMPTY);
				cell.setEditable(true);
				data.add(cell);
			}
			rows.add(data);
			rws++;
		}
		return rows;
	}

	/**
	 * @param metamodel
	 * @return
	 * @throws Exception
	 */
	private ObservableList<SpreadsheetCell> generateHeaderCells() {
		final ObservableList<SpreadsheetCell> headers = FXCollections.observableArrayList();
		final String[] fieldNames = this.getFieldOrder();
		SpreadsheetCell cell = null;
		int column = 0;
		for (String name : fieldNames) {
			cell = SpreadsheetCellType.STRING.createCell(0, column++, 1, 1, metamodel.getTitle(name));
			cell.setEditable(false);
			headers.add(cell);
		}
		return headers;
	}

	/**
	 * @param row
	 * @param editable
	 */
	protected void setRowEditable(final ObservableList<SpreadsheetCell> row, final boolean editable) {
		row.forEach(c -> c.setEditable(editable));
	}

}
