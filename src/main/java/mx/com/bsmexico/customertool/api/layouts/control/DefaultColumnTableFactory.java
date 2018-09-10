package mx.com.bsmexico.customertool.api.layouts.control;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutMetaModel;

public class DefaultColumnTableFactory<S> extends ColumnTableFactoryAbstract<S> {

	public DefaultColumnTableFactory(LayoutMetaModel<S> metamodel) throws IllegalArgumentException {
		super(metamodel);
	}

	public DefaultColumnTableFactory(Class<S> type) throws IllegalArgumentException {
		super(type);
	}

	@Override
	public <T> TableColumn<S, T> getColumn(String fieldName, int width) throws Exception {
		final TableColumn<S, T> column = new TableColumn<>();
		column.setVisible(!metamodel.isHidden(fieldName));
		Label firstNameLabel = new Label(metamodel.getTitle(fieldName));
		column.setGraphic(firstNameLabel);
		column.setId(fieldName);
		column.setPrefWidth(width);
		column.setCellValueFactory(new PropertyValueFactory<S, T>(metamodel.getClassFieldName(fieldName)));
		return column;
	}

}
