package mx.com.bsmexico.customertool.api.process;

import java.io.File;

/**
 * @author jchr
 *
 */
public interface Exporter<T> {

	void export(File file) throws Exception;
}
