package mx.com.bsmexico.customertool.api.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public abstract class FixPositionImporter<T> implements Importer<T> {
	private ImportTarget<T> target;

	public FixPositionImporter(final ImportTarget<T> target) throws IllegalArgumentException {
		if (target == null) {
			throw new IllegalArgumentException("Target can not be null");
		}
		this.target = target;
	}

	@Override
	public void importFile(File file) throws Exception {
		if (file == null) {
			throw new IllegalArgumentException("file can not be null");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException("file not exist");
		}
		if (!file.canRead()) {
			throw new IllegalArgumentException("File can not be read");
		}
		List<T> data = new ArrayList<>();
		final List<FixPosition> positions = this.getFixPositions();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")))) {
			T instance = null;
			String line = reader.readLine();
			while (line != null) {
				instance = this.getInstance(getRecord(line, positions));
				if (instance != null) {
					data.add(instance);
				}
				line = reader.readLine();
			}
			this.target.setData(data);
		}
	}

	private List<String> getRecord(final String line, final List<FixPosition> positions) throws Exception {
		final List<String> record = new ArrayList<String>();
		if (StringUtils.isBlank(line) || (positions == null || positions.isEmpty())) {
			record.add(line);
		} else {
			final StringBuffer buffer = new StringBuffer(line);
			positions.forEach(p -> {
				if (p.start < 0 || p.start > line.length() - 1 || p.end <= 0 || p.end > line.length() - 1
						|| p.start > p.end) {
					throw new IllegalArgumentException("Illegal position[" + p.start + "-" + p.end + "]");
				} else {
					record.add(buffer.substring(p.start, p.end));
				}
			});
		}
		return record;
	}

	/**
	 * @param obj
	 * @return
	 */
	protected abstract T getInstance(List<String> record);

	/**
	 * @param record
	 * @return
	 */
	protected abstract List<FixPosition> getFixPositions();

	/**
	 * @author jchr
	 *
	 */
	public static class FixPosition {
		private int start;
		private int end;

		public FixPosition(final int start, final int end) {
			this.start = start;
			this.end = end;
		}

		public int getStart() {
			return start;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public int getEnd() {
			return end;
		}

		public void setEnd(int end) {
			this.end = end;
		}

	}

}
