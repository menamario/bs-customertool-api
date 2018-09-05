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
import mx.com.bsmexico.customertool.api.layouts.model.LayoutMetaModel;

/**
 * @author jchr
 *
 */
public abstract class LayoutTable<T> extends Region {
	protected TableView<T> table;
	protected ColumnTableFactoryAbstract<T> columnFactory;
	protected final ObservableList<T> data = FXCollections.observableArrayList();
	protected LayoutMetaModel<T> metamodel;
	protected Class<T> type;

	protected LayoutTable(final Class<T> type) {
		this(type, null);
	}

	/**
	 * @param layoutFactory
	 * @throws IllegalArgumentException
	 */
	public LayoutTable(final Class<T> type, final ColumnTableFactoryAbstract<T> columnFactory)
			throws IllegalArgumentException, InstantiationError {
		super();
		if (type == null) {
			throw new IllegalArgumentException("Type can not be null");
		}
		if (columnFactory == null) {
			throw new IllegalArgumentException("Factories can not be null");
		}
		this.columnFactory = columnFactory;
		metamodel = new LayoutMetaModel<T>(type);
		this.columnFactory = (columnFactory == null) ? new ColumnTableFactoryAbstract<T>(metamodel) {
		} : columnFactory;
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
			if (pos != null && (event.getCode().isLetterKey() || event.getCode().isDigitKey())) {
				table.edit(pos.getRow(), pos.getTableColumn());
			}
			if (event.getCode() == KeyCode.TAB) {
				table.requestFocus();
				if ((pos.getColumn() + 1) == table.getColumns().size()
						&& (pos.getRow() + 1) == table.getItems().size()) {
					addRow();
				}

				KeyCode kc;
				if (event.isShiftDown())
					kc = KeyCode.LEFT;
				else
					kc = KeyCode.RIGHT;

				KeyEvent ke = new KeyEvent(table, table, KeyEvent.KEY_PRESSED, "", "", kc, false, false, false, false);
				table.fireEvent(ke);

				if ((pos.getColumn() + 1) == table.getColumns().size()) {
					table.getSelectionModel().selectNext();
					table.scrollToColumnIndex(0);
				}
			}
		});
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
		String[] ids = getFieldOrder();
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

	/**
	 * @return
	 */
	protected String[] getFieldOrder() {
		return metamodel.getFieldNames().toArray(new String[0]);
	}

	/**
	 * 
	 */
	protected abstract void polulate();

	/**
	 * 
	 */
	protected abstract void addRow();
}
