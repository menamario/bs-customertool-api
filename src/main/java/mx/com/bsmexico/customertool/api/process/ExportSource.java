package mx.com.bsmexico.customertool.api.process;

import java.util.List;

/**
 * @author jchr
 *
 * @param <T>
 */
public interface ExportSource <T>{

	List<T> getData();	
}
