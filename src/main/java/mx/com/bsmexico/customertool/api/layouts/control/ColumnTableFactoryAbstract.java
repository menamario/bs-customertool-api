package mx.com.bsmexico.customertool.api.layouts.control;

import javafx.scene.control.TableColumn;
import mx.com.bsmexico.customertool.api.layouts.model.LayoutMetaModel;

/**
 * @author jchr
 *
 * @param <S>
 */
public abstract class ColumnTableFactoryAbstract<S> {
	protected Class<S> type;
	protected LayoutMetaModel<S> metamodel;

	/**
	 * @param type
	 * @throws IllegalArgumentException
	 */
	public ColumnTableFactoryAbstract(final Class<S> type) throws IllegalArgumentException {
		if (type == null) {
			throw new IllegalArgumentException("Type can not be null");
		}		
		this.type = type;
		this.metamodel = new LayoutMetaModel<S>(type);
	}
	
	/**
	 * @param type
	 * @throws IllegalArgumentException
	 */
	public ColumnTableFactoryAbstract(LayoutMetaModel<S> metamodel) throws IllegalArgumentException {
		if (metamodel == null) {
			throw new IllegalArgumentException("Metamodelo can not be null");
		}
		this.metamodel = metamodel;
		this.type = metamodel.getModel();
	}
	
	public abstract <T> TableColumn<S,T> getColumn(final String fieldName, final int width) throws Exception;

}
