package mx.com.bsmexico.customertool.api.report;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class ReportDataSourceFactory {

	private ReportDataSourceFactory() {
	}

	public static JRDataSource getCsvDataSource(final String csvFile, final String[] columnNames,
			final boolean firstRowAsHeader) throws IllegalArgumentException, IOException {
		JRDataSource dataSource = null;
		if (StringUtils.isBlank(csvFile)) {
			throw new IllegalArgumentException("Csv file can not be null");
		}

		if (columnNames == null || columnNames.length == 0) {
			throw new IllegalArgumentException("Column names can not be null or empty");
		}
		try {
			final InputStream csv = JRLoader.getLocationInputStream(csvFile);
			final JRCsvDataSource csvDataSource = new JRCsvDataSource(csv);
			csvDataSource.setRecordDelimiter("\r\n");
			csvDataSource.setUseFirstRowAsHeader(firstRowAsHeader);
			csvDataSource.setColumnNames(columnNames);
		} catch (JRException exception) {
			throw new IOException(exception);
		}

		return dataSource;
	}

	/**
	 * @param beans
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static <T> JRDataSource getBeanDataSource(final T[] beans) throws IllegalArgumentException {
		if (beans == null || beans.length == 0) {
			throw new IllegalArgumentException("Beans can not be null or empty");
		}

		return new JRBeanArrayDataSource(beans);
	}

	/**
	 * @param beans
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static <T> JRDataSource getBeanDataSource(final List<T> beans) throws IllegalArgumentException {
		if (beans == null || beans.size() == 0) {
			throw new IllegalArgumentException("Beans can not be null or empty");
		}
		return new JRBeanCollectionDataSource(beans);
	}

}