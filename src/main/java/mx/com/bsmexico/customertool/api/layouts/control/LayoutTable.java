package mx.com.bsmexico.customertool.api.layouts.control;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutMetaModel;

/**
 * @author jchr
 *
 */
public abstract class LayoutTable<T> extends TableView<T> {
	protected ColumnTableFactoryAbstract<T> columnFactory;
	protected final ObservableList<T> data = FXCollections.observableArrayList();
	protected LayoutMetaModel<T> metamodel;
	protected Class<T> type;
	protected boolean validated = false;
	protected boolean hasIndexColumn;

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
		this.type = type;
	}

	/**
	 * @return the inEvaluation
	 */
	public boolean isValidated() {
		return validated;
	}

	/**
	 * Search a column of the table by Id
	 * 
	 * @param id
	 * @return
	 */
	protected TableColumn<T, ?> getColumn(final String id) {
		TableColumn<T, ?> column = null;
		if (StringUtils.isNotBlank(id)) {
			final Optional<TableColumn<T, ?>> result = this.getColumns().stream().filter(c -> id.equals(c.getId()))
					.findFirst();
			if (result.isPresent()) {
				column = result.get();
			}
		}
		return column;
	}

	/**
	 * Set de index column
	 */
	protected void setIndexColumn() {
		TableColumn<T, String> indexColumn = new TableColumn<>();
		indexColumn.setPrefWidth(40);
		indexColumn.setResizable(false);
		indexColumn.setText("#");		
		indexColumn.setCellFactory(new Callback<TableColumn<T, String>, TableCell<T, String>>() {
			@Override
			public TableCell<T, String> call(TableColumn<T, String> param) {
				return new TableCell<T, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (this.getTableRow() != null ) {
							setText(this.getTableRow().getIndex() + 1 + "");
						} else {
							setText("");
						}
					}
				};
			}
		});
		this.getColumns().add(0, indexColumn);
		hasIndexColumn = true;
	}

	
	/**
	 * 
	 */
	protected void removeIndexColumn() {
		if(hasIndexColumn) {
			this.getColumns().remove(0);
		}
	}

	/**
	 * 
	 */
	public abstract boolean validateTable() throws Exception;

	public abstract boolean validateModel(T model) throws Exception;

	public abstract boolean isActiveModel(T model) throws Exception;
	/**
	 * @return
	 */
	// public TableView<T> getTable() {
	// return this.table;
	// }

	/**
	 * @throws Exception
	 */
	protected abstract void setColumns() throws Exception;

	/**
	 * @return
	 */
	protected abstract String[] getFieldOrder();

	/**
	 * 
	 */
	protected abstract void polulate();

	/**
	 * 
	 */
	protected abstract void addRow();

}
