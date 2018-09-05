package mx.com.bsmexico.customertool.api.layouts.model.validation;

import java.util.List;

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

}
