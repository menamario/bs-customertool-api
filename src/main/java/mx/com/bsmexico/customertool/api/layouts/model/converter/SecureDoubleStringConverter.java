package mx.com.bsmexico.customertool.api.layouts.model.converter;

import org.apache.commons.lang3.math.NumberUtils;

import javafx.util.converter.DoubleStringConverter;

/**
 * @author jchr
 *
 */
public class SecureDoubleStringConverter extends DoubleStringConverter {

	/**
	 * {@inheritDoc}}
	 * 
	 * Check if is a number before converting
	 * 
	 */
	@Override
	public Double fromString(String number) {
		return NumberUtils.isCreatable(number) ? super.fromString(number) : null;
	}

}
