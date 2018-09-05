package mx.com.bsmexico.customertool.api.layouts.control;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import mx.com.bsmexico.customertool.api.layouts.modell.LayoutMetaModel;

/**
 * @author jchr
 *
 * @param <S>
 */
public abstract class ColumnTableFactoryAbstract<S> {
	private Class<S> type;
	private LayoutMetaModel<S> metamodel;

	/**
	 * @param type
	 * @throws IllegalArgumentException
	 */
	public ColumnTableFactoryAbstract(Class<S> type) throws IllegalArgumentException {
		if (type == null) {
			throw new IllegalArgumentException("Type can not be null");
		}
		this.type = type;
		metamodel = new LayoutMetaModel<S>(type);
	}

	/**
	 * @param field
	 * @param typeContentClass
	 * @param converter
	 * @param width
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> TableColumn<S, T> getInstance(final String fieldName, final int width) throws Exception {

		final TableColumn<S, T> column = new TableColumn<>();
		Label firstNameLabel = new Label(metamodel.getTitle(fieldName));
		if (metamodel.getRestrictionDesc(fieldName)!=null)
			firstNameLabel.setTooltip(new Tooltip(metamodel.getRestrictionDesc(fieldName)));
	    column.setGraphic(firstNameLabel);
		
		column.setId(fieldName);
		final StringConverter converter = (metamodel.getConverter(fieldName) == null)
				? (StringConverter) new DefaultStringConverter()
				: metamodel.getConverter(fieldName);
		column.setPrefWidth(width);
		final Predicate<T> restiction = metamodel.getRestriction(fieldName);
		final Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory = new Callback<TableColumn<S, T>, TableCell<S, T>>() {
			public TableCell<S, T> call(TableColumn<S, T> p) {
				final TextFieldEditCell<S, T> cell = new TextFieldEditCell<S, T>(converter, restiction, metamodel.getClassFieldName(fieldName), metamodel.getFieldIds());
				if(metamodel.isDisabled(fieldName)) {
					cell.setDisable(true);
					cell.setStyle("-fx-background-color: gray;");
				}
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

	/**
	 * @return
	 */
	public Set<String> getFieldIds() {
		return (this.metamodel == null) ? new HashSet<>() : this.metamodel.getFieldNames();
	}

}
