package mx.com.bsmexico.customertool.api.layouts.model.validation;

public class LayoutValidatorException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5504273202734520472L;
	private Object entity;

	/**
	 * @param message
	 */
	public LayoutValidatorException(final String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param entity
	 */
	public LayoutValidatorException(final String message, final Object entity) {
		super(message);
		this.entity = entity;
	}

	/**
	 * @return the entity
	 */
	public Object getEntity() {
		return entity;
	}

}
