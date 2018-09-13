package mx.com.bsmexico.customertool.api.process;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jchr
 *
 * @param <T>
 */
public abstract class FixPositionExporter<T> implements Exporter<T> {

	protected ExportSource<T> source;

	/**
	 * @param source
	 */
	public FixPositionExporter(ExportSource<T> source) {
		if (source == null) {
			throw new IllegalArgumentException("Source can not be null");
		}
		this.source = source;
	}

	@Override
	public void export(File file) throws Exception {
		final List<T> data = source.getData();
		final BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()));
		if (data != null) {
			for(T elem : data) {
				final List<RecordPosition> record = this.getRecord(elem);
				String line = StringUtils.EMPTY;
				for(RecordPosition pos : record) {
					line = this.placeValue(pos.getStart(), pos.getEnd(),pos.getValue(), line);
				}
				writer.write(line);
				writer.newLine();
			}			
		}
		writer.flush();
		writer.close();
	}

	/**
	 * @param value
	 * @param line
	 * @param offset
	 */
	private String placeValue(final int start, final int end, final String value, final String line) {
		final StringBuffer buffer = new StringBuffer((line == null) ? StringUtils.EMPTY : line);
		final int limit = buffer.length() - 1;
		if (start >= 0 && end > start) {
			if (start > limit) {
				buffer.append(StringUtils.rightPad("", start - limit));
			}
			if(end > (buffer.length() -1)) {
				buffer.insert(start, StringUtils.rightPad(value, end - start));
			}else {
				buffer.replace(start, end, StringUtils.rightPad(value, end - start));
			}
		}
		return buffer.toString();
	}

	/**
	 * @param obj
	 * @return
	 */
	protected abstract List<RecordPosition> getRecord(T obj);

}
