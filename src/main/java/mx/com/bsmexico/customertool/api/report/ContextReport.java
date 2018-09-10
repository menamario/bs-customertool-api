package mx.com.bsmexico.customertool.api.report;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jchr
 *
 */
public class ContextReport {

	private Map<String, Object> params;
	private ReportType type;

	/**
	 * 
	 */
	public ContextReport() {
		this(ReportType.PDF, null);
	}

	/**
	 * 
	 */
	public ContextReport(final Map<String, Object> initParams) {
		this(ReportType.PDF, initParams);
	}

	/**
	 * 
	 */
	public ContextReport(final ReportType type, final Map<String, Object> initParams) {
		params = new HashMap<>();
		if (initParams != null) {
			params.putAll(initParams);
		}
		this.type = type;
	}

	/**
	 * @param key
	 * @param value
	 */
	private void add(final String key, Object value) {
		this.params.put(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final String value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final Boolean value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final Integer value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final Date value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final Byte value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final Double value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final Float value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final Long value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final BigDecimal value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(final String key, final Short value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addImageParameter(final String key, final File value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addImageParameter(final String key, final InputStream value) {
		add(key, value);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void addImageParameter(final String key, final URL value) {
		add(key, value);
	}

	/**
	 * @return
	 */
	public Map<String, Object> getParameters() {
		return this.params;
	}

	/**
	 * @return
	 */
	public ReportType getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(ReportType type) {
		this.type = type;
	}

}
