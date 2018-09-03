package mx.com.bsmexico.customertool.api.exporter;

import java.util.List;

/**
 * @author jchr
 *
 * @param <T>
 */
public interface ExportSource <T>{

	List<T> getData();	
}
