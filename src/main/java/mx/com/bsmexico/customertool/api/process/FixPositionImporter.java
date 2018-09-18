package mx.com.bsmexico.customertool.api.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * @author jchr
 *
 * @param <T>
 */
public abstract class FixPositionImporter<T> implements Importer<T> {
	private ImportTarget<T> target;
	private boolean trim;

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
		final List<RecordPosition> positions = this.getFixPositions();
		
		String strTipoCod=null;
        try {
			strTipoCod=this.detectarCodificacion(file);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (strTipoCod==null){
            strTipoCod="ISO-8859-1";
        }
		
		
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), strTipoCod))) {
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

	/**
	 * @param trim
	 */
	public void withTrim(final boolean trim) {
		this.trim = trim;
	}

	/**
	 * @param line
	 * @param positions
	 * @return
	 * @throws Exception
	 */
	private List<String> getRecord(final String line, final List<RecordPosition> positions) throws Exception {
		final List<String> record = new ArrayList<String>();
		if (StringUtils.isBlank(line) || (positions == null || positions.isEmpty())) {
			record.add(line);
		} else {
			positions.forEach(p -> {
				final String part = extractValue(p.getStart(), p.getEnd(), line, trim);
				record.add((part == null) ? StringUtils.EMPTY : part);
			});
		}
		return record;
	}

	/**
	 * Extract the sequence of character in according to position (start-end)
	 * 
	 * @param line
	 * @return
	 */
	private String extractValue(final int start, final int end, final String line, final boolean trim) {
		String value = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(line)) {			
			final int limit = line.length();
			if (start >= 0 && start < limit && end > start && end <= limit) {
				value = StringUtils.substring(line, start, end);
			}
		}
		return (trim) ? value.trim() : value;
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
	protected abstract List<RecordPosition> getFixPositions();
	
	public String detectarCodificacion(File strRutaArchivoP) throws Throwable{
        byte[] buf;
        java.io.FileInputStream fis;
        UniversalDetector detector;
        int nread;
        String encoding=null;
        try{    
            buf= new byte[4096];
            fis = new java.io.FileInputStream(strRutaArchivoP);
            detector = new UniversalDetector(null);
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
              detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            encoding = detector.getDetectedCharset();
            detector.reset();
            return encoding;
        }catch (IOException e) {
            System.out.println("Error en - Clase(" + new Exception().getStackTrace()[0].getClassName() + ") "
                      + "- Método(" + new Exception().getStackTrace()[0].getMethodName() + ") \n" + e);
        }
		return encoding;
}
	
}
