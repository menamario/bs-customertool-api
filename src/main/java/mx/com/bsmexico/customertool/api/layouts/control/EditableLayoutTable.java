package mx.com.bsmexico.customertool.api.layouts.control;

import org.apache.commons.lang3.ArrayUtils;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
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
public abstract class EditableLayoutTable<T> extends LayoutTable<T> {

	protected EditableLayoutTable(Class<T> type) {
		super(type, new EditableColumnTableFactory<>(type));
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
				ct = columnFactory.<String>getColumn(id, 100);
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
			//this.table = new TableView<T>();
			setColumns();
			polulate();
			setItems(data);
			//getChildren().add(this.table);
			getSelectionModel().setCellSelectionEnabled(true);
			setTableEditable();
			setStyle("-fx-selection-cell: red; -fx-selection-bar-non-focused: salmon;");
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new InstantiationError(exception.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void setTableEditable() {
		setEditable(true);
		// allows the individual cells to be selected
		getSelectionModel().cellSelectionEnabledProperty().set(true);
		// when character or numbers pressed it will start edit in editable
		// fields
		final TableView table = this;
		setOnKeyPressed(event -> {
			TablePosition<T, ?> pos = table.getFocusModel().getFocusedCell();
			
			if(pos!=null && event.getCode()==KeyCode.DELETE){
             //TODO implementar este pedo
			}else if(pos!=null && event.getCode()==KeyCode.C && event.isControlDown()){
				 final Clipboard clipboard = Clipboard.getSystemClipboard();
			     final ClipboardContent content = new ClipboardContent();
			     content.putString(pos.getTableColumn().getCellData(pos.getRow()).toString());
			     clipboard.setContent(content);
			}else if (pos!=null && event.getCode()==KeyCode.V && event.isControlDown()){
				final Clipboard clipboard = Clipboard.getSystemClipboard();
				final ClipboardContent content = new ClipboardContent();
				String cb = clipboard.getString();
				content.put(DataFormat.HTML, cb);
				clipboard.setContent(content);
				table.edit(pos.getRow(), pos.getTableColumn());
			}else if (pos != null && (event.getCode().isLetterKey() || event.getCode().isDigitKey())) {
				final Clipboard clipboard = Clipboard.getSystemClipboard();
				final ClipboardContent content = new ClipboardContent();
				content.put(DataFormat.URL, event.getText());
				clipboard.setContent(content);
                table.edit(pos.getRow(), pos.getTableColumn());
			}
			if (event.getCode() == KeyCode.TAB) {
				table.requestFocus();
				if ((pos.getColumn() + 1) == table.getColumns().size()
						&& (pos.getRow() + 1) == table.getItems().size()) {
					addRow();
					refresh();
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
	 * @return
	 */
	@Override
	protected String[] getFieldOrder() {
		return metamodel.getFieldNames().toArray(new String[0]);
	}
	
	

}
