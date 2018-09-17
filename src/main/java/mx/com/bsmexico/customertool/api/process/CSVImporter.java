package mx.com.bsmexico.customertool.api.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author jchr
 *
 * @param <T>
 */
public abstract class CSVImporter<T> implements Importer<T> {

	private ImportTarget<T> target;
	private DecimalFormat df = new DecimalFormat("###.#");
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
			case "XLSX":
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
		/*final Workbook w = Workbook.getWorkbook(file);
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
		}*/	
		int lastRowNum = 0;
		int maxRowWidth = 0;
		int lastCellNum = 0;
		try (FileInputStream fis = new FileInputStream(file)) {
			final Workbook wb = WorkbookFactory.create(fis);
			int numSheets = wb.getNumberOfSheets();
			Sheet sheet = null;						
			Cell cell = null;			
			Row row = null;
			String value = StringUtils.EMPTY;
			for (int i = 0; i < numSheets; i++) {
				sheet = wb.getSheetAt(i);
				if (sheet.getPhysicalNumberOfRows() > 0) {
					lastRowNum = sheet.getLastRowNum();
					final String[] headers = getHeader();
					for (int j = (headers == null) ? 0 : 1; j <= lastRowNum; j++) {
						row = sheet.getRow(j);
						final ArrayList<String> values = new ArrayList<>();
						if (row != null) {
							lastCellNum = row.getLastCellNum();
							for (int ic = 0; ic <= lastCellNum; ic++) {
								cell = row.getCell(ic);
								if (cell == null) {
									values.add("");
								} else {
									switch (cell.getCellType()) {
									case Cell.CELL_TYPE_STRING:
										value = cell.getStringCellValue();
										break;
									case Cell.CELL_TYPE_NUMERIC:
										value = df.format(cell.getNumericCellValue());
										break;
									case Cell.CELL_TYPE_BOOLEAN:
										value = Boolean.toString(cell.getBooleanCellValue());
										break;
									default:
										value = StringUtils.EMPTY; break;
									}									
									//value = cell.getStringCellValue();
									values.add(value.trim());
								}
							}
							if (lastCellNum > maxRowWidth) {
								maxRowWidth = lastCellNum;
							}
						}
						records.add(values);
					}
				}
			}
		}
		completeRecords(records, maxRowWidth);
		return records;
	}
	
	/**
	 * @param records
	 * @param maxRowWidth
	 */
	private void completeRecords(final List<List<String>> records, final int maxRowWidth) {
		if (records != null && records.size() > 0) {
			for (List<String> record : records) {
				if (record != null) {
					if (record.size() < maxRowWidth) {
						for (int indx = record.size(); indx <= maxRowWidth; indx++) {
							record.add(StringUtils.EMPTY);
						}
					}
				}
			}
		}
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
