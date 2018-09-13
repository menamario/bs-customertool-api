package mx.com.bsmexico.customertool.api.process;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jchr
 *
 */
public class RecordPosition {
	private int start;
	private int end;
	private String value;

	public RecordPosition(final int start, final int end, final Object value) {
		this.start = (start < 0) ? 0 : start;
		this.end = (end < start) ? start : end;
		this.value = (value == null) ? StringUtils.EMPTY : value.toString();
	}

	public RecordPosition(final int start, final int end) {
		this(start, end, StringUtils.EMPTY);
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

}