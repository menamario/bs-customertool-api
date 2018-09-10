package mx.com.bsmexico.customertool.api.layouts.model.validation;

import java.util.List;

/**
 * @author jchr
 *
 * @param <S>
 */
public abstract class LayoutModelValidator<S> {


	/**
	 * 
	 * @param fieldName
	 * @param model
	 * @return
	 */
	public abstract boolean isValidField(String fieldName, S model);

	/**
	 * @param fieldName
	 * @return
	 */
	public abstract String getValidationDescription(String fieldName);

	/**
	 * @param model
	 * @return
	 */
	public abstract boolean isValid(S model);

	/**
	 * @param models
	 * @return
	 */
	public abstract boolean isValid(List<S> models);
}
