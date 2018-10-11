package mx.com.bsmexico.customertool.api.layouts.control;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * @author jchr
 *
 * @param <T>
 */
public abstract class EditableLayoutTable<T> extends LayoutTable<T> {
	T copiedRow = null;

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
				ct.impl_setReorderable(false);
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
			setStyle("-fx-selection-cell: red; -fx-selection-bar-non-focused: salmon;");
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new InstantiationError(exception.getMessage());
		}
	}

	public void moveToCell(TablePosition pos) {
		final TableView<T> table = this;
		Bounds bCell = pos.getTableColumn().getGraphic()
				.localToScene(pos.getTableColumn().getGraphic().getBoundsInLocal());
		Bounds bTable = table.localToScene(table.getBoundsInLocal());
		ScrollBar sc = (ScrollBar) table.queryAccessibleAttribute(AccessibleAttribute.HORIZONTAL_SCROLLBAR);
		double sv = sc.getValue();
		double dif = bCell.getMinX() - bTable.getMinX();
		if (dif < 46) {
			double restar = 46 - dif;
			sv -= restar;
			sc.setValue(sv);
		}
	}

	@SuppressWarnings("unchecked")
	private void setTableEditable() {
		ContextMenu cm = new ContextMenu();
		setEditable(true);
		// allows the individual cells to be selected
		getSelectionModel().cellSelectionEnabledProperty().set(true);
		final TableView<T> table = this;

		table.getFocusModel().focusedCellProperty().addListener((obs, oldSelection, newSelection) -> {

			if (newSelection != null && oldSelection.getColumn() != 0) {
				Platform.runLater(() -> {
					cm.hide();
					if (newSelection != null && newSelection.getTableColumn() != null
							&& newSelection.getTableColumn().getGraphic() != null) {
						moveToCell(newSelection);
					} else if (newSelection.getColumn() == 0 && oldSelection.getColumn()!=-1) {
						table.getSelectionModel().selectRightCell();
					}
				});
			}
		});

		MenuItem removeItem = new MenuItem("Eliminar registro");
		cm.getItems().add(removeItem);
		MenuItem copyItem = new MenuItem("Copiar registro");
		cm.getItems().add(copyItem);
		MenuItem pasteItem = new MenuItem("Pegar registro");
		cm.getItems().add(pasteItem);

		addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent t) {

				TablePosition<T, ?> pos = table.getFocusModel().getFocusedCell();

				cm.setHideOnEscape(true);

				removeItem.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						table.getItems().remove(pos.getRow());
					}
				});

				copyItem.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						copiedRow = table.getItems().get(pos.getRow());
					}
				});

				pasteItem.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						T another;
						try {
							another = type.newInstance();
							BeanUtils.copyProperties(another, copiedRow);
							table.getItems().set(pos.getRow(), another);
							table.getSelectionModel().select(pos.getRow(), pos.getTableColumn());
						} catch (InstantiationException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

				if (t.getButton() == MouseButton.SECONDARY) {
					pasteItem.setDisable(copiedRow == null);
					cm.show(table, t.getScreenX(), t.getScreenY());
				}
			}
		});

		// when character or numbers pressed it will start edit in editable
		// fields

		setOnKeyPressed(event -> {
			TablePosition<T, ?> pos = table.getFocusModel().getFocusedCell();

			if (pos.getTableColumn().isEditable()) {
				if (pos != null && event.getCode() == KeyCode.DELETE) {
					ObservableValue observableValue = this.getColumns().get(pos.getColumn())
							.getCellObservableValue(pos.getRow());
					((StringProperty) observableValue).set("");
					// TODO implementar este pedo
				} else if (pos != null && event.getCode() == KeyCode.C && event.isControlDown()) {
					final Clipboard clipboard = Clipboard.getSystemClipboard();
					final ClipboardContent content = new ClipboardContent();
					content.putString(pos.getTableColumn().getCellData(pos.getRow()).toString());
					clipboard.setContent(content);
				} else if (pos != null && event.getCode() == KeyCode.V && event.isControlDown()) {
					final Clipboard clipboard = Clipboard.getSystemClipboard();
					ObservableValue observableValue = this.getColumns().get(pos.getColumn())
							.getCellObservableValue(pos.getRow());
					((StringProperty) observableValue).set(clipboard.getString());
				} else if (pos != null && (event.getCode().isLetterKey() || event.getCode().isDigitKey())) {
					final Clipboard clipboard = Clipboard.getSystemClipboard();
					final ClipboardContent content = new ClipboardContent();
					content.put(DataFormat.URL, "layouts" + event.getText());
					clipboard.setContent(content);
					moveToCell(pos);
					table.edit(pos.getRow(), pos.getTableColumn());
				}
			}

			if (event.getCode() == KeyCode.TAB) {
				moveToCell(pos);

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
				Platform.runLater(() -> {
					table.requestFocus();
				});

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
