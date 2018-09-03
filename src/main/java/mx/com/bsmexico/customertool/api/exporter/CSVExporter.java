package mx.com.bsmexico.customertool.api.exporter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * @author jchr
 *
 * @param <T>
 */
public abstract class CSVExporter <T> implements Exporter<T>{
	/* (non-Javadoc)
	 * @see mx.com.bsmexico.layoutstool.core.api.layouts.Exporter#export()
	 */
	@Override
	public void export(final List<T> records, File file) throws Exception{		
		Writer writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(getHeader()));        
        if(records != null) {
        	records.forEach(r ->{
        		try {
					csvPrinter.printRecord(getRecord(r));
				} catch (IOException e) {
					//do nothig
				}
        	});
        }
        csvPrinter.flush();
        csvPrinter.close();		
	}

	/**
	 * @param obj
	 * @return
	 */
	protected abstract Object[] getRecord(T obj);
	
	/**
	 * @return
	 */
	protected abstract String[] getHeader();
}
