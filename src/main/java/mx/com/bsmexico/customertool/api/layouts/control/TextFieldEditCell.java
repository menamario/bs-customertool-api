package mx.com.bsmexico.customertool.api.layouts.control;

import javafx.application.Platform;
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
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import mx.com.bsmexico.customertool.api.layouts.model.validation.LayoutModelValidator;

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
	private LayoutModelValidator<S> validator;
	private String fieldName;
	private int maxLength;
	private boolean validationError = false;

	/**
	 * @param converter
	 */
	public TextFieldEditCell(final String field) {
		super();
		this.fieldName = field;
	}

	/**
	 * @param converter
	 */
	public TextFieldEditCell(final String field, final StringConverter<T> converter) {
		super(converter);
		this.fieldName = field;

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
		((EditableLayoutTable) getTableView()).moveToCell(tablePos);
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
		((EditableLayoutTable) getTableView()).moveToCell(tablePos);

	}

	/** {@inheritDoc} */
	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		updateItem();
	}

	/**
	 * @param validator
	 */
	public void setValidator(final LayoutModelValidator<S> validator) {
		this.validator = validator;
	}

	/**
	 * @param maxLength
	 */
	public void setmaxLength(final int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * 
	 */

	/**
	 * @return
	 */
	private TextField getTextField() {

		final TextField textField = new TextField(getItemText());
		textField.setStyle("-fx-border-width:0; -fx-padding:0");

		textField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// nothing
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
			} else if (/* event.getCode() == KeyCode.RIGHT || */ event.getCode() == KeyCode.TAB) {
				event.consume();
				getTableView().fireEvent(new KeyEvent(getTableView(), getTableView(), KeyEvent.KEY_PRESSED, "", "",
						KeyCode.TAB, false, false, false, false));
				((EditableLayoutTable) getTableView()).moveToCell(tablePos);
			} else if (/* event.getCode() == KeyCode.RIGHT || */ event.getCode() == KeyCode.ENTER) {
				event.consume();
				Platform.runLater(() -> {
					getTableView().getSelectionModel().selectBelowCell();
				});

			} /*
				 * else if (event.getCode() == KeyCode.LEFT) { event.consume();
				 * getTableView().fireEvent(new KeyEvent(getTableView(),
				 * getTableView(), KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT,
				 * false, false, false, false)); } else if (event.getCode() ==
				 * KeyCode.UP) { event.consume(); getTableView().fireEvent(new
				 * KeyEvent(getTableView(), getTableView(),
				 * KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false,
				 * false, false)); } else if (event.getCode() == KeyCode.DOWN) {
				 * event.consume(); getTableView().fireEvent(new
				 * KeyEvent(getTableView(), getTableView(),
				 * KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN, false, false,
				 * false, false)); }
				 */
		});

		if (this.maxLength > 0) {
			textField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(final ObservableValue<? extends String> ov, final String oldValue,
						final String newValue) {
					if (textField.getText().length() > maxLength) {
						String s = textField.getText().substring(0, maxLength);
						textField.setText(s);
					}
				}
			});
		}

		return textField;
	}

	/**
	 * @return
	 */
	private String getItemText() {
		return getConverter() == null ? getItem() == null ? "" : getItem().toString()
				: getConverter().toString(getItem());
	}

	@SuppressWarnings("unchecked")
	private void executeValidation() {
		if (this.getTableView() instanceof LayoutTable) {
			if (((LayoutTable<S>) this.getTableView()).isValidated() && isEditable()) {
				this.getStyleClass().remove("invalid");
				if (this.getTooltip() != null) {
					this.setTooltip(null);
				}

				final S registro = (S) this.getTableRow().getItem();
				if (validator.isActive(registro) && !validator.isValidField(fieldName, registro)) {
					this.getStyleClass().add("invalid");
					Tooltip tt = new Tooltip(validator.getValidationDescription(fieldName));
					tt.setStyle(
							"-fx-text-fill: black;-fx-background-color: lightgray;-fx-font-family: FranklinGothicLT;-fx-font-size: 12px;");
					this.setTooltip(tt);
				}
			}
		}
	}

	/**
	 * 
	 */
	private void updateItem() {
		executeValidation();
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
				this.toBack();
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

		final Clipboard clipboard = Clipboard.getSystemClipboard();
		String cbUrl = clipboard.getUrl();
		if (cbUrl != null && cbUrl.startsWith("layouts")) {
			Platform.runLater(() -> {
				textField.setText(cbUrl.substring(7));
				clipboard.clear();
				textField.requestFocus();
				textField.end();
				
			});
		} else {
			textField.selectAll();
			Platform.runLater(() -> {
				textField.requestFocus();
			});
		}

		// requesting focus so that key input can immediately go into the
		// TextField

	}

}
