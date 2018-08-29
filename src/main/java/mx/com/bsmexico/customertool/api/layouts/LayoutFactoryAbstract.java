package mx.com.bsmexico.customertool.api.layouts;

import java.io.InputStream;

import mx.com.bsmexico.customertool.api.layouts.model.Layout;

/**
 * @author jchr
 *
 */
public abstract class LayoutFactoryAbstract {

	protected InputStream layout;

	protected LayoutFactoryAbstract(final InputStream layout) throws IllegalArgumentException {
		if (layout == null) {
			throw new IllegalArgumentException("The layout can not be null");
		}
		this.layout = layout;
	}

	/**
	 * Get Layout instance
	 * 
	 * @return
	 */
	public abstract Layout getLayoutInstance() throws Exception;
}