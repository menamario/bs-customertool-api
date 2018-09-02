package mx.com.bsmexico.customertool.api.layouts.modell.converter;

import org.apache.commons.lang3.math.NumberUtils;

import javafx.util.converter.IntegerStringConverter;

public class SecureIntegerStringConverter extends IntegerStringConverter {

	/**
	 * {@inheritDoc}}
	 * 
	 * Check if is a number before converting
	 */
	@Override
	public Integer fromString(String number) {
		return NumberUtils.isCreatable(number) ? super.fromString(number) : null;
	}

}
