package mx.com.bsmexico.customertool.api.layouts.control;

import org.apache.commons.lang3.ArrayUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

/**
 * @author jchr
 *
 */
public abstract class LayoutTable<T> extends Region {
	protected TableView<T> table;
	protected ColumnTableFactoryAbstract<T> columnFactory;
	protected final ObservableList<T> data = FXCollections.observableArrayList();
	
	/**
	 * @param layoutFactory
	 * @throws IllegalArgumentException
	 */
	public LayoutTable(final ColumnTableFactoryAbstract<T> columnFactory)
			throws IllegalArgumentException, InstantiationError {
		super();
		if (columnFactory == null) {
			throw new IllegalArgumentException("Factories can not be null");
		}
		this.columnFactory = columnFactory;
		init();
	}

	/**
	 * 
	 * @throws InstantiationError
	 */
	protected void init() throws InstantiationError {
		try {
			this.table = this.createTable();
			setColumns();
			polulate();
			this.table.setItems(data);
			getChildren().add(this.table);
			this.table.getSelectionModel().setCellSelectionEnabled(true);
			setTableEditable();
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new InstantiationError(exception.getMessage());
		}
	}

	private void setTableEditable() {
		table.setEditable(true);
		// allows the individual cells to be selected
		table.getSelectionModel().cellSelectionEnabledProperty().set(true);
		// when character or numbers pressed it will start edit in editable
		// fields
		
        table.setOnKeyPressed(event -> {
            TablePosition<T, ?> pos = table.getFocusModel().getFocusedCell();
            if (pos != null && (event.getCode().isLetterKey()||event.getCode().isDigitKey())) {
                table.edit(pos.getRow(), pos.getTableColumn());
            }
            if (event.getCode()==KeyCode.TAB){
                table.requestFocus();
                if ((pos.getColumn()+1)==table.getColumns().size() 
                        && (pos.getRow()+1)==table.getItems().size()){
                    addRow();
                }
                
                KeyCode kc;
                if (event.isShiftDown()) kc = KeyCode.LEFT;
                else kc = KeyCode.RIGHT;
                
                KeyEvent ke = new KeyEvent(table, table, KeyEvent.KEY_PRESSED, "", "", kc, false,false,false,false);
                table.fireEvent(ke);
                
                if ((pos.getColumn()+1)==table.getColumns().size()){
                    table.getSelectionModel().selectNext();
                    table.scrollToColumnIndex(0);
                }
            }
        });

		
		
//		table.setOnKeyPressed(event -> {
//			if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
//				editFocusedCell();
//			} else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.TAB) {
//				table.getSelectionModel().selectNext();
//				event.consume();
//			} else if (event.getCode() == KeyCode.LEFT) {
//				selectPrevious();
//				event.consume();
//			}
//		});
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void editFocusedCell() {
		final TablePosition<T, ?> focusedCell = table.focusModelProperty().get().focusedCellProperty().get();
		table.edit(focusedCell.getRow(), focusedCell.getTableColumn());
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void selectPrevious() {
		if (table.getSelectionModel().isCellSelectionEnabled()) {
			TablePosition<T, ?> pos = table.getFocusModel().getFocusedCell();
			if (pos.getColumn() - 1 >= 0) {
				table.getSelectionModel().select(pos.getRow(), getTableColumn(pos.getTableColumn(), -1));
			} else if (pos.getRow() < table.getItems().size()) {
				table.getSelectionModel().select(pos.getRow() - 1,
						table.getVisibleLeafColumn(table.getVisibleLeafColumns().size() - 1));
			}
		} else {
			int focusIndex = table.getFocusModel().getFocusedIndex();
			if (focusIndex == -1) {
				table.getSelectionModel().select(table.getItems().size() - 1);
			} else if (focusIndex > 0) {
				table.getSelectionModel().select(focusIndex - 1);
			}
		}
	}

	/**
	 * @param column
	 * @param offset
	 * @return
	 */
	private TableColumn<T, ?> getTableColumn(final TableColumn<T, ?> column, int offset) {
		int columnIndex = table.getVisibleLeafIndex(column);
		int newColumnIndex = columnIndex + offset;
		return table.getVisibleLeafColumn(newColumnIndex);
	}

	/**
	 * @param layout
	 * @return
	 * @throws Exception
	 */
	protected TableView<T> createTable() throws Exception {
		final TableView<T> table = new TableView<T>();
		return table;
	}

	/**
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void setColumns() throws Exception {
		String[] ids = getFieldOrder();//this.columnFactory.getFieldIds();  
		if (!ArrayUtils.isEmpty(ids)) {
			TableColumn ct = null;
			for (String id : ids) {
				ct = columnFactory.getInstance(id, 100);
				ct.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
				table.getColumns().add(ct);
			}
		}
	}

	public TableView<T> getTable() {
		return this.table;
	}

	protected String[] getFieldOrder() {
		return columnFactory.getFieldIds().toArray(new String[0]);
	}
	
	protected abstract void polulate();

	protected abstract void addRow();
}
