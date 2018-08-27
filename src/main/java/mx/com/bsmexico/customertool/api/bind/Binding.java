package mx.com.bsmexico.customertool.api.bind;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jchr
 *
 * @param <T>
 */
public interface Binding<T> {
	/**
	 * @param file that will be converted to Java object
	 * @return
	 * @throws Exception
	 */
	T unmarshall(final InputStream file) throws Exception;

	/**
	 * @param obj    Object that will be converted to data format suitable for
	 *               storage or transmission.
	 * @param output XML will be added to this stream
	 * @throws Exception
	 */
	void marshall(T obj, OutputStream output) throws Exception;
}
