package mx.com.bsmexico.customertool.api.exporter;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * @author jchr
 *
 * @param <T>
 */
public abstract class CSVImporter<T> implements Importer<T>{
	
	ImportTarget<T> target;
	
	public CSVImporter(final ImportTarget<T> target) throws IllegalArgumentException{
		if(target == null) {
			throw  new IllegalArgumentException("Target can not be null");
		}		
		this.target = target;
	}

	@Override
	public void importFile(File file) throws Exception {		
		List<T> data = new ArrayList<>();
		if (file != null) {			
			final BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()));
			final String[] headers = getHeader();
			CSVFormat format = (headers == null) ? CSVFormat.DEFAULT : CSVFormat.DEFAULT.withHeader(headers);
			final CSVParser csvParser = new CSVParser(reader, format.withIgnoreHeaderCase().withTrim());
			if (csvParser != null) {
				for (CSVRecord csvRecord : csvParser) {
					data.add(this.getInstance(csvRecord));
				}
			}
		}		
		target.setData(data);
	}

	/**
	 * @param obj
	 * @return
	 */
	protected abstract T getInstance(final CSVRecord record);

	/**
	 * @return
	 */
	protected abstract String[] getHeader();

}
