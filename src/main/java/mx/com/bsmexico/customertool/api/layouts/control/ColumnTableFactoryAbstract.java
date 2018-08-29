package mx.com.bsmexico.customertool.api.layouts.control;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import mx.com.bsmexico.customertool.api.layouts.LayoutFieldModel;
import mx.com.bsmexico.customertool.api.layouts.LayoutModel;
import mx.com.bsmexico.customertool.api.layouts.model.Field;

/**
 * @author jchr
 *
 * @param <S>
 */
public abstract class ColumnTableFactoryAbstract<S extends LayoutModel> {
	private Class<S> type;
	private java.lang.reflect.Field[] classFields;

	/**
	 * @param type
	 * @throws IllegalArgumentException
	 */
	public ColumnTableFactoryAbstract(Class<S> type) throws IllegalArgumentException {
		if (type == null) {
			throw new IllegalArgumentException("Type can not be null");
		}
		this.type = type;
	}

	
	/**
	 * @param field
	 * @param typeContentClass
	 * @param converter
	 * @param width
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> TableColumn<S, T> getInstance(final Field field, final Class<T> typeContentClass, final StringConverter<T> converter, final int width)
			throws Exception {

		final TableColumn<S, T> column = new TableColumn<>(field.getTitle());
		final StringConverter<T> finalConverter = (converter == null) ? (StringConverter<T>) new DefaultStringConverter() : converter;
		column.setMinWidth(width);
		final java.lang.reflect.Field property = this.getProperty(field.getId());
		final Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory = new Callback<TableColumn<S, T>, TableCell<S, T>>() {
			public TableCell<S, T> call(TableColumn<S, T> p) {
				return new TextFieldEditCell<S, T>(finalConverter);
			}
		};
		column.setCellValueFactory(new PropertyValueFactory<S, T>(property.getName()));
		column.setCellFactory(cellFactory);
		column.setOnEditCommit(new EventHandler<CellEditEvent<S, T>>() {
			@Override
			public void handle(CellEditEvent<S, T> t) {
				S row = ((S) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try {
					Method method = type.getDeclaredMethod("set" + StringUtils.capitalize(property.getName()),
							typeContentClass);
					method.invoke(row, t.getNewValue());
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		return column;
	}
	
	/**
	 * @param field
	 * @param typeContentClass
	 * @param width
	 * @return
	 * @throws Exception
	 */
	public <T> TableColumn<S, T> getInstance(final Field field, final Class<T> typeContentClass, final int width)
			throws Exception {
		return this.getInstance(field, typeContentClass,null, width);
	}

	/**
	 * @param field
	 * @return
	 */
	/**
	 * @param field
	 * @return
	 */
	private java.lang.reflect.Field getProperty(String field) {
		java.lang.reflect.Field property = null;
		if (classFields == null) {
			classFields = type.getDeclaredFields();
		}

		for (java.lang.reflect.Field cf : classFields) {
			LayoutFieldModel annotation = cf.getDeclaredAnnotation(LayoutFieldModel.class);
			if (annotation != null && annotation.field().equals(field)) {
				property = cf;
				break;
			}
		}
		return property;
	}
}
