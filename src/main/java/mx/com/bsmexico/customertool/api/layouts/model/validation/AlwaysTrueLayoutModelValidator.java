package mx.com.bsmexico.customertool.api.layouts.model.validation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class AlwaysTrueLayoutModelValidator extends LayoutModelValidator<Object> {

	@Override
	public boolean isValidField(String fieldName, Object model) {
		return true;
	}

	@Override
	public boolean isValid(Object model) {
		return true;
	}

	@Override
	public boolean isValid(List<Object> models) {
		return true;
	}

	@Override
	public String getValidationDescription(String fieldName) {
		return StringUtils.EMPTY;
	}

	@Override
	public boolean isActive(Object model) {
		// TODO Auto-generated method stub
		return true;
	}

}
