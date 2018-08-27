package mx.com.bsmexico.customertool.api.bind;

/**
 * @author jchr
 *
 */
public interface XMLBinding<T> extends Binding<T> {
	/**
	 * Force to validate subsequent unmarshal operations.
	 * 
	 * @param validating
	 */
	void setValidating(final boolean validating);
}
