package mx.com.bsmexico.customertool.api.importer;

import java.util.List;

/**
 * @author jchr
 *
 */
public interface ImportTarget<T> {
	/**
	 * @param data
	 */
	void setData(List<T> data);
}
