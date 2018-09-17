package mx.com.bsmexico.customertool.api.layouts.control;

import org.apache.commons.lang3.ArrayUtils;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author jchr
 *
 * @param <T>
 */
public abstract class DefaultLayoutTable<T> extends LayoutTable<T> {

	protected DefaultLayoutTable(Class<T> type) {
		super(type, new DefaultColumnTableFactory<>(type));
		init();
	}

	/**
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void setColumns() throws Exception {
		String[] ids = getFieldOrder();
		if (!ArrayUtils.isEmpty(ids)) {
			TableColumn ct = null;
			for (String id : ids) {
				ct = columnFactory.getColumn(id, 100);
				ct.prefWidthProperty().bind(widthProperty().multiply(0.15));
				getColumns().add(ct);
			}
		}
	}

	/**
	 * 
	 * @throws InstantiationError
	 */
	protected void init() throws InstantiationError {
		try {
			// this.table = new TableView<T>();
			setColumns();
			polulate();
			setItems(data);
			// getChildren().add(this.table);
			getSelectionModel().setCellSelectionEnabled(true);
			setTableEditable();
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new InstantiationError(exception.getMessage());
		}
	}

	/**
	 * 
	 */
	private void setTableEditable() {
		setEditable(true);
		setOnKeyPressed(event -> {
			TablePosition<T, ?> pos = getFocusModel().getFocusedCell();

			if (pos != null && event.getCode() == KeyCode.C && event.isControlDown()) {
				final Clipboard clipboard = Clipboard.getSystemClipboard();
				final ClipboardContent content = new ClipboardContent();
				content.putString(pos.getTableColumn().getCellData(pos.getRow()).toString());
				clipboard.setContent(content);
			}
		});
	}

	/**
	 * @return
	 */
	@Override
	protected String[] getFieldOrder() {
		return metamodel.getFieldNames().toArray(new String[0]);
	}

}
