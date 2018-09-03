package mx.com.bsmexico.customertool.api.exporter;

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
