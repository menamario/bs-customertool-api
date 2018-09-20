package mx.com.bsmexico.customertool.api.layouts.control;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	 * 
	 */
	public abstract boolean validateTable() throws Exception;
	
	public abstract boolean validateModel(T model) throws Exception;
	
	public abstract boolean isActiveModel(T model) throws Exception;
	/**
	 * @return
	 */
	//public TableView<T> getTable() {
		//return this.table;
	//}

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
