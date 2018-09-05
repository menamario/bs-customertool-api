package mx.com.bsmexico.customertool.api.layouts.model.converter;

import org.apache.commons.lang3.math.NumberUtils;

import javafx.util.converter.LongStringConverter;

public class SecureLongStringConverter extends LongStringConverter {

	/**
	 * {@inheritDoc}
	 * 
	 * Check if is a number before converting
	 **/
	@Override
	public Long fromString(String number) {
		return NumberUtils.isCreatable(number) ? super.fromString(number) : null;
	}

}
