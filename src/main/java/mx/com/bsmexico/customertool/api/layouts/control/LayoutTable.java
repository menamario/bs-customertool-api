package mx.com.bsmexico.customertool.api.layouts.control;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
			this.table.getSelectionModel().setCellSelectionEnabled(true);
			
			table.setOnKeyPressed(event -> {
	            TablePosition<T, ?> pos = table.getFocusModel().getFocusedCell();
	            if (pos != null && (event.getCode().isLetterKey()||event.getCode().isDigitKey())) {
	                table.edit(pos.getRow(), pos.getTableColumn());
	            }
	            if (event.getCode()==KeyCode.TAB){
	            	table.requestFocus();
	            	if ((pos.getColumn()+1)==table.getColumns().size() 
	            			&& (pos.getRow()+1)==table.getItems().size()){
	            		addRow();
	            	}
	            	
	            	KeyCode kc;
	            	if (event.isShiftDown()) kc = KeyCode.LEFT;
	            	else kc = KeyCode.RIGHT;
	            	
	            	KeyEvent ke = new KeyEvent(table, table, KeyEvent.KEY_PRESSED, "", "", kc, false,false,false,false);
	            	table.fireEvent(ke);
	            	
	            	if ((pos.getColumn()+1)==table.getColumns().size()){
					    table.getSelectionModel().selectNext();
					    table.scrollToColumnIndex(0);
			        }
	            }
	        });
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
				
				 TableColumn ct = columnFactory.getInstance(f, String.class, 100);
				 //TODO incluir en la configuracion de las columnas que porcentaje de la tabla debe ocupar
	             ct.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
	             table.getColumns().add(ct);
			}
		}
		return table;
	}

	protected abstract void polulate();
	
	protected abstract void addRow();
}
