package mx.com.bsmexico.customertool.api.importer;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * @author jchr
 *
 * @param <T>
 */
public abstract class CSVImporter<T> implements Importer<T> {

	ImportTarget<T> target;

	public CSVImporter(final ImportTarget<T> target) throws IllegalArgumentException {
		if (target == null) {
			throw new IllegalArgumentException("Target can not be null");
		}
		this.target = target;
	}

	@Override
	public void importFile(final File file) throws Exception {
		List<T> data = new ArrayList<>();
		if (file != null) {
			String extension = FilenameUtils.getExtension(file.getName()).toUpperCase();
			List<List<String>> records = null;
			// CSVFormat format = null;
			switch (extension) {
			case "XLS":
				records = getRecordsFromExcel(file);
				break;
			default:
				records = getRecordsFromStandarCSV(file);
				break;
			}

			if (records != null) {
				T instance = null;
				for (List<String> record : records) {
					instance = this.getInstance(record);
					if (instance != null) {
						data.add(instance);
					}
				}
			}
		}
		target.setData(data);
	}

	/**
	 * @param file
	 * @return
	 */
	private List<List<String>> getRecordsFromExcel(final File file) throws Exception {
		final List<List<String>> records = new ArrayList<>();
		final Workbook w = Workbook.getWorkbook(file);
		final Sheet sheet = w.getSheet(0);
		List<String> values = null;
		String value = null;
		final String[] headers = getHeader();
		for (int j = (headers == null) ? 0 : 1; j < sheet.getRows(); j++) {
			values = new ArrayList<>();
			for (int i = 0; i < sheet.getColumns(); i++) {
				Cell cell = sheet.getCell(i, j);
				value = cell.getContents();
				values.add((StringUtils.isBlank(value) ? StringUtils.EMPTY : value.trim()));
			}
			records.add(values);
		}
		return records;
	}

	/**
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private List<List<String>> getRecordsFromStandarCSV(final File file) throws Exception {
		final List<List<String>> records = new ArrayList<>();
		final BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()));
		final String[] headers = getHeader();
		CSVFormat format = CSVFormat.DEFAULT;
		if (headers != null) {
			format = format.withHeader(headers);
		}
		if (getCustomDelimiter() != null) {
			format = format.withDelimiter(getCustomDelimiter());
		}
		format = format.withIgnoreHeaderCase().withTrim();
		try (CSVParser csvParser = new CSVParser(reader, format)) {
			if (csvParser != null) {
				List<String> values = null;
				for (CSVRecord csvRecord : csvParser) {
					values = new ArrayList<>();
					Iterator<String> itr = csvRecord.iterator();
					while (itr.hasNext()) {
						values.add(itr.next());
					}
					records.add(values);
				}
			}
		}
		return records;
	}

	/**
	 * @param obj
	 * @return
	 */
	protected abstract T getInstance(List<String> records);

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