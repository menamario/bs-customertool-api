package mx.com.bsmexico.customertool.api.layouts;

import java.io.File;
import java.util.List;

/**
 * @author jchr
 *
 */
public interface Exporter<T> {

	void export(List<T> records, File file) throws Exception;
}
