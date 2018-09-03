package mx.com.bsmexico.customertool.api.exporter;

import java.io.File;

/**
 * @author jchr
 *
 */
public interface Importer<T> {

	void importFile(File file) throws Exception;
}
