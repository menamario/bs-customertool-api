package mx.com.bsmexico.customertool.api.process;

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
public abstract class CSVExporter<T> implements Exporter<T> {
	protected ExportSource<T> source;

	/**
	 * @param source
	 */
	public CSVExporter(ExportSource<T> source) {
		if (source == null) {
			throw new IllegalArgumentException("Source can not be null");
		}
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.com.bsmexico.layoutstool.core.api.layouts.Exporter#export()
	 */
	@Override
	public void export(final File file) throws Exception {
		final List<T> records = source.getData();
		final Writer writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()));
		CSVFormat format = CSVFormat.DEFAULT;
		final String[] header = getHeader();
		if(header != null && header.length > 0) {
			format = format.withHeader(header);
		}		
		if(getCustomDelimiter() != null) {
			format = format.withDelimiter(getCustomDelimiter());
		}		
		CSVPrinter csvPrinter = new CSVPrinter(writer, format);		
		if (records != null) {
			records.forEach(r -> {
				try {
					csvPrinter.printRecord(getRecord(r));
				} catch (IOException e) {
					// do nothig
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

	/**
	 * @return
	 */
	protected Character getCustomDelimiter() {
		return null;
	}
}