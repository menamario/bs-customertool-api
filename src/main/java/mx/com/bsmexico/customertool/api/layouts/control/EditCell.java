package mx.com.bsmexico.customertool.api.layouts.control;

import javafx.event.Event;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

/**
 * @author jchr
 *
 * @param <S> The type of the TableView (Model) : TableView<S>
 * @param <T> The type of the content in the cell
 */
public abstract class EditCell<S, T> extends TableCell<S, T> {

	protected Control control;
	protected boolean escapePressed = false;
	private TablePosition<S, ?> tablePos = null;

	

	/**
	 * 
	 */
	public EditCell() {
		super();
	}

	@Override
	public void startEdit() {
		if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
			return;
		}
		super.startEdit();

		if (isEditing()) {
			if (control == null) {
				control = getCellControl(getItem());
			}
			escapePressed = false;
			if (control != null) {
				setControlValue();
			}
			setText(null);
			setGraphic(control);
			// requesting focus so that key input can immediately go into the
			// TextField
			control.requestFocus();
			final TableView<S> table = getTableView();
			tablePos = table.getEditingCell();
		}
	}

	/**
	 * Return the value of the cell that is displayed in the view when the cell is
	 * not being edited. By default this method return the content of the cell to
	 * string.
	 * 
	 * @return
	 */
	private String getVisualValue() {
		return getVisualValueCell(getItem());
	}

	/**
	 * Set the value to cell Control. See getCellControl()
	 */
	private void setControlValue() {
		setControlValue(getItem());
	}

	/**
	 * Return the Control to edit the cell value. For example, if the value of the
	 * cell will be edited with the help of a control TextField, TexField should be
	 * returned
	 *
	 * @param cellContent . Current content <T> of the cell
	 * @return
	 */
	protected abstract Control getCellControl(final T cellContent);

	/**
	 * Set the value to the cell Control. See getControlCell()
	 * 
	 * @param cellContent . Current content <T> of the cell
	 */
	protected abstract void setControlValue(T cellContent);

	/**
	 * Return the value of the cell Control. See getControlCell()
	 * 
	 * @param cellContent . Current content <T> of the cell
	 */
	protected abstract Object getControlValue(T cellContent);

	/**
	 * @param cellContent Current content <T> of the cell
	 * @return
	 */
	protected abstract String getVisualValueCell(T cellContent);

	/** {@inheritDoc} */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		updateItem(newValue, false);
		if (table != null) {
			// reset the editing cell on the TableView
			table.edit(-1, null);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void cancelEdit() {
		if (escapePressed) {
			// this is a cancel event after escape key
			super.cancelEdit();
			setText(getVisualValue()); // restore the original text in the view
		} else {
			// this is not a cancel event after escape key
			// we interpret it as commit.
			// String newText = textField.getText();
			// commit the new text to the model
			// this.commitEdit(getConverter().fromString(newText));
			this.commitEdit(getItem());
		}
		setGraphic(null); // stop editing with TextField
	}

	/** {@inheritDoc} */
	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		updateItem();
	}

	private void updateItem() {
		if (isEmpty()) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (control != null) {
					setControlValue();
				}
				setText(null);
				setGraphic(control);
			} else {
				setText(getVisualValue());
				setGraphic(null);
			}
		}
	}
}
