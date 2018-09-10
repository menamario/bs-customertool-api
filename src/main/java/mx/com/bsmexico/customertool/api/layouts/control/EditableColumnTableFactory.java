package mx.com.bsmexico.customertool.api.layouts.control;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutMetaModel;
import mx.com.bsmexico.customertool.api.layouts.model.validation.LayoutModelValidator;

public class EditableColumnTableFactory<S> extends ColumnTableFactoryAbstract<S> {

	public EditableColumnTableFactory(LayoutMetaModel<S> metamodel) throws IllegalArgumentException {
		super(metamodel);
	}

	public EditableColumnTableFactory(Class<S> type) throws IllegalArgumentException {
		super(type);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> TableColumn<S, T> getColumn(String fieldName, int width) throws Exception {
		final TableColumn<S, T> column = new TableColumn<>();
		Label firstNameLabel = new Label(metamodel.getTitle(fieldName));
		column.setGraphic(firstNameLabel);

		column.setId(fieldName);
		final StringConverter converter = (metamodel.getConverter(fieldName) == null)
				? (StringConverter) new DefaultStringConverter()
				: metamodel.getConverter(fieldName);
		final LayoutModelValidator<S> validator = (LayoutModelValidator<S>) metamodel.getValidator();
		column.setPrefWidth(width);
		final Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory = new Callback<TableColumn<S, T>, TableCell<S, T>>() {
			public TableCell<S, T> call(TableColumn<S, T> p) {
				final TextFieldEditCell<S, T> cell = new TextFieldEditCell<S, T>(fieldName, converter);
				cell.setValidator(validator);
				if (metamodel.isDisabled(fieldName)) {
					cell.setDisable(true);
					cell.setStyle("-fx-background-color: gray;");
				}
				cell.setmaxLength(metamodel.getLength(fieldName));
				return cell;
			}
		};
		column.setCellValueFactory(new PropertyValueFactory<S, T>(metamodel.getClassFieldName(fieldName)));
		column.setCellFactory(cellFactory);
		column.setOnEditCommit(new EventHandler<CellEditEvent<S, T>>() {
			@Override
			public void handle(CellEditEvent<S, T> t) {
				final S row = ((S) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				final T value = (t.getNewValue() == null) ? t.getOldValue() : t.getNewValue();
				if (value != null) {
					try {
						final Method method = type.getDeclaredMethod(
								"set" + StringUtils.capitalize(metamodel.getClassFieldName(fieldName)),
								(metamodel.getWrapperClass(fieldName) == null) ? String.class
										: metamodel.getWrapperClass(fieldName));
						method.invoke(row, value);
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
				t.getTableView().refresh();
			}
		});
		return column;
	}

}
