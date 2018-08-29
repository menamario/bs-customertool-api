package mx.com.bsmexico.customertool.api.layouts.control;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import mx.com.bsmexico.customertool.api.layouts.LayoutFactoryAbstract;
import mx.com.bsmexico.customertool.api.layouts.LayoutModel;
import mx.com.bsmexico.customertool.api.layouts.model.Field;
import mx.com.bsmexico.customertool.api.layouts.model.Layout;

/**
 * @author jchr
 *
 */
public abstract class LayoutTable<T extends LayoutModel> extends Region {
	protected LayoutFactoryAbstract factory;
	public TableView<T> table;
	protected ColumnTableFactoryAbstract<T> columnFactory;
	protected final ObservableList<T> data = FXCollections.observableArrayList();

	/**
	 * @param layoutFactory
	 * @throws IllegalArgumentException
	 */
	public LayoutTable(final LayoutFactoryAbstract layoutFactory, final ColumnTableFactoryAbstract<T> columnFactory)
			throws IllegalArgumentException, InstantiationError {
		super();
		if (layoutFactory == null || columnFactory == null) {
			throw new IllegalArgumentException("Factories can not be null");
		}
		this.columnFactory = columnFactory;
		factory = layoutFactory;
		init();
	}

	/**
	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		// TODO poner las correctas dimensiones, quizá relacionado a las columnas y
		// diseño
		// setPadding(new Insets(10, 10, 10, 10));
	}

	/**
	 * @throws InstantiationError
	 */
	protected void init() throws InstantiationError {
		try {
			Layout layout = factory.getLayoutInstance();
			this.table = this.createTable(layout);
			polulate();
			this.table.setItems(data);
			getChildren().add(this.table);
			this.table.setEditable(true);
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new InstantiationError(exception.getMessage());
		}
	}

	/**
	 * @param layout
	 * @return
	 * @throws Exception
	 */
	private TableView<T> createTable(final Layout layout) throws Exception {
		final TableView<T> table = new TableView<T>();
		if (layout != null) {
			List<Field> fields = layout.getFields().getField();
			for (Field f : fields) {
				table.getColumns().add(columnFactory.getInstance(f, String.class, 100));
			}
		}
		return table;
	}

	protected abstract void polulate();
}
