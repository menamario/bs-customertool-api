package mx.com.bsmexico.customertool.api.exporter;

import java.io.File;
import java.util.List;

/**
 * @author jchr
 *
 */
public interface Exporter<T> {

	void export(List<T> records, File file) throws Exception;
}
