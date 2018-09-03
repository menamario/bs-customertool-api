package mx.com.bsmexico.customertool.api.layouts.control;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * @author jchr
 *
 * @param <S>
 * @param <T>
 */
public class TextFieldEditCell<S, T> extends TextFieldTableCell<S, T> {

	private TextField textField;
	private boolean escapePressed = false;
	private TablePosition<S, ?> tablePos = null;
	private Predicate<T> restriction;
	private String fieldName = null;
	private List<String> fields;

	/**
	 * @param converter
	 */
	public TextFieldEditCell(final StringConverter<T> converter) {
		super(converter);
	}

	/**
	 * @param converter
	 */
	public TextFieldEditCell(final StringConverter<T> converter, Predicate<T> restriction, String fieldName, List<String> fields) {
		super(converter);
		this.restriction = restriction;
		this.fieldName = fieldName;
		this.fields = fields;
		// 
		// this.setStyle("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.scene.control.cell.TextFieldTableCell#startEdit()
	 */
	@Override
	public void startEdit() {
		if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
			return;
		}
		super.startEdit();

		if (isEditing()) {
			if (textField == null) {
				textField = getTextField();
			}
			escapePressed = false;
			startEdit(textField);
			final TableView<S> table = getTableView();
			tablePos = table.getEditingCell();
		}
	}

	/** {@inheritDoc} */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void commitEdit(T newValue) {
		if (!isEditing())
			return;
		final TableView<S> table = getTableView();
		if (table != null) {
			// Inform the TableView of the edit being ready to be committed.
			CellEditEvent editEvent = new CellEditEvent(table, tablePos, TableColumn.editCommitEvent(), newValue);

			Event.fireEvent(getTableColumn(), editEvent);
		}
		// we need to setEditing(false):
		super.cancelEdit(); // this fires an invalid EditCancelEvent.
		// update the item within this cell, so that it represents the new value

		if (table != null) {
			// reset the editing cell on the TableView
			table.edit(-1, null);
		}
		//evaluateRestriction(newValue);
		updateItem(newValue, false);
	}

	/** {@inheritDoc} */
	@Override
	public void cancelEdit() {
		if (escapePressed) {
			// this is a cancel event after escape key
			super.cancelEdit();
			setText(getItemText()); // restore the original text in the view
		} else {
			// this is not a cancel event after escape key
			// we interpret it as commit.
			String newText = textField.getText();
			// commit the new text to the model
			this.commitEdit(getConverter().fromString(newText));
		}
		setGraphic(null); // stop editing with TextField
	}

	/** {@inheritDoc} */
	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		updateItem();
	}

	/**
	 * @param item
	 */
	private void evaluateRestriction(T item) {
		if (restriction != null) {
			TableView<S> table = getTableView();
			S registro = (S) table.getItems().get(tablePos.getRow());
			
			
			
			
			Method method = null;
			try {
				method = registro.getClass().getDeclaredMethod("setEstatus", String.class,
						Boolean.class);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Method m = null;
			try {
				m = registro.getClass().getDeclaredMethod("getInvalids");
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<String> listaInvalidos;
			try {
				listaInvalidos = (List<String>) m.invoke(registro);
				for(String s: listaInvalidos){
					method.invoke(registro, s, false);
					fields.remove(s);
				}
				for(String s: fields){
					method.invoke(registro, s, true);
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				method.invoke(registro, fieldName, restriction.test(item));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
	}

	/**
	 * @return
	 */
	private TextField getTextField() {

		final TextField textField = new TextField(getItemText());

		textField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("hi");
			}
		});

		// Use onAction here rather than onKeyReleased (with check for Enter),
		textField.setOnAction(event -> {
			if (getConverter() == null) {
				throw new IllegalStateException("StringConverter is null.");
			}
			this.commitEdit(getConverter().fromString(textField.getText()));
			event.consume();
		});

		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					commitEdit(getConverter().fromString(textField.getText()));
				}
			}
		});

		textField.setOnKeyPressed(t -> {
			if (t.getCode() == KeyCode.ESCAPE)
				escapePressed = true;
			else
				escapePressed = false;
		});
		textField.setOnKeyReleased(t -> {
			if (t.getCode() == KeyCode.ESCAPE) {
				throw new IllegalArgumentException("did not expect esc key releases here.");
			}
		});

		textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				textField.setText(getConverter().toString(getItem()));
				cancelEdit();
				event.consume();
			} else if (/*event.getCode() == KeyCode.RIGHT ||*/ event.getCode() == KeyCode.TAB) {
				event.consume();
				getTableView().fireEvent(new KeyEvent(getTableView(), getTableView(), KeyEvent.KEY_PRESSED, "", "",
						KeyCode.TAB, false, false, false, false));
			}/* else if (event.getCode() == KeyCode.LEFT) {
				event.consume();
				getTableView().fireEvent(new KeyEvent(getTableView(), getTableView(), KeyEvent.KEY_PRESSED, "", "",
						KeyCode.LEFT, false, false, false, false));
			} else if (event.getCode() == KeyCode.UP) {
				event.consume();
				getTableView().fireEvent(new KeyEvent(getTableView(), getTableView(), KeyEvent.KEY_PRESSED, "", "",
						KeyCode.UP, false, false, false, false));
			} else if (event.getCode() == KeyCode.DOWN) {
				event.consume();
				getTableView().fireEvent(new KeyEvent(getTableView(), getTableView(), KeyEvent.KEY_PRESSED, "", "",
						KeyCode.DOWN, false, false, false, false));
			}*/
		});

		return textField;
	}

	/**
	 * @return
	 */
	private String getItemText() {
		return getConverter() == null ? getItem() == null ? "" : getItem().toString()
				: getConverter().toString(getItem());
	}

	/**
	 * 
	 */
	private void updateItem() {
		this.getStyleClass().remove("invalid");
		S registro = (S) this.getTableRow().getItem();
		if (registro != null) {
			try {
				final Method method = registro.getClass().getDeclaredMethod("getEstatus");
				Map<String, Boolean> map = (Map<String, Boolean>) method.invoke(registro);
				
				final Method mt = registro.getClass().getDeclaredMethod("getTooltip", String.class);
				String tooltip = (String) mt.invoke(registro,fieldName);
				if (map.get(fieldName).equals(Boolean.FALSE)) {
					this.getStyleClass().add("invalid");
					if (tooltip!=null)this.setTooltip(new Tooltip(tooltip));
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isEmpty()) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getItemText());
				}
				setText(null);
				setGraphic(textField);
			} else {
				setText(getItemText());
				setGraphic(null);
			}
		}
	}

	/**
	 * @param textField
	 */
	private void startEdit(final TextField textField) {
		if (textField != null) {
			textField.setText(getItemText());
		}
		setText(null);
		setGraphic(textField);
		textField.selectAll();
		// requesting focus so that key input can immediately go into the
		// TextField
		textField.requestFocus();
	}
}
