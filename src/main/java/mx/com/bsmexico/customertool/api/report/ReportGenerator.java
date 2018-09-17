package mx.com.bsmexico.customertool.api.report;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class ReportGenerator {

	private ReportGenerator() {
	}

	/**
	 * @param jrxmlFile
	 * @param context
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	public static void generateFromTemplate(final String jrxmlFile, final ContextReport context,
			final JRDataSource dataSource, final OutputStream output) throws Exception {
		if (StringUtils.isBlank(jrxmlFile)) {
			throw new IllegalArgumentException("Jasper file can not be null");
		}

		if (output == null) {
			throw new IllegalArgumentException("Output can not be null");
		}
		final InputStream reportTemplate = JRLoader.getLocationInputStream(jrxmlFile);
		JasperDesign design = JRXmlLoader.load(reportTemplate);
		generateReport(JasperCompileManager.compileReport(design), (context == null) ? new ContextReport() : context,
				(dataSource == null) ? new JREmptyDataSource() : dataSource, output);
	}

	/**
	 * @param jasperFile
	 * @param context
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	public static void generateFromCompiledReport(final String jasperFile, final ContextReport context,
			final JRDataSource dataSource, final OutputStream output) throws Exception {
		if (StringUtils.isBlank(jasperFile)) {
			throw new IllegalArgumentException("Jasper file can not be null");
		}

		if (output == null) {
			throw new IllegalArgumentException("Output can not be null");
		}
		final InputStream report = JRLoader.getLocationInputStream(jasperFile);
		JasperReport reportObject = (JasperReport) JRLoader.loadObject(report);
		generateReport(reportObject, (context == null) ? new ContextReport() : context,
				(dataSource == null) ? new JREmptyDataSource() : dataSource, output);
	}

	/**
	 * @param report
	 * @param context
	 * @param dataSource
	 * @param output
	 * @return
	 * @throws Exception
	 */
	private static void generateReport(final JasperReport report, final ContextReport context,
			final JRDataSource dataSource, final OutputStream output) throws Exception {
		final JasperPrint print = fillReport(report, context, dataSource, output);
		switch (context.getType()) {
		case PDF:
			pdf(print, output);
			break;
		case XLS:
			xls(print, output);
			break;
		case XLSX:
			xlsx(print, output);
		}
	}

	/**
	 * @param report
	 * @param context
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	private static JasperPrint fillReport(final JasperReport report, final ContextReport context,
			final JRDataSource dataSource, final OutputStream output) throws Exception {
		return JasperFillManager.fillReport(report,
				(context == null) ? new HashMap<String, Object>() : context.getParameters(),
				(dataSource == null) ? new JREmptyDataSource() : dataSource);
	}

	/**
	 * @param print
	 * @throws JRException
	 */
	private static void pdf(final JasperPrint print, final OutputStream output) throws Exception {
		JasperExportManager.exportReportToPdfStream(print, output);
	}

	/**
	 * @param print
	 * @throws JRException
	 */
	private static void xls(final JasperPrint print, final OutputStream output) throws JRException {
		JRXlsExporter exporter = new JRXlsExporter();

		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(false);
		configuration.setDetectCellType(true);
		configuration.setCollapseRowSpan(false);
		configuration.setIgnoreCellBackground(true);
		configuration.setWhitePageBackground(false);
		configuration.setAutoFitPageHeight(true);
		configuration.setRemoveEmptySpaceBetweenRows(true);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}

	/**
	 * @param print
	 * @throws JRException
	 */
	private static void xlsx(final JasperPrint print, final OutputStream output) throws JRException {

		JRXlsxExporter exporter = new JRXlsxExporter();

		exporter.setExporterInput(new SimpleExporterInput(print));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setOnePagePerSheet(true);
		configuration.setDetectCellType(true);
		configuration.setCollapseRowSpan(false);
		configuration.setIgnoreCellBackground(true);
		configuration.setWhitePageBackground(false);
		configuration.setAutoFitPageHeight(true);
		configuration.setRemoveEmptySpaceBetweenRows(true);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}
}
