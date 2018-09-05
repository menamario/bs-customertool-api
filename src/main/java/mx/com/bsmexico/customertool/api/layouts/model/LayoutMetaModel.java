package mx.com.bsmexico.customertool.api.layouts.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotationUtils;

import javafx.util.StringConverter;
import mx.com.bsmexico.customertool.api.layouts.model.validation.LayoutModelValidator;

/**
 * @author jchr
 *
 */
public class LayoutMetaModel<T> {
	private static final String METAMODEL_VALIDATOR = "validatorClass";
	private static final String METAMODEL_WRAPPER_CLASS = "wrappedClass";
	private static final String METAMODEL_CONVERT_CLASS = "converterClass";
	private static final String METAMODEL_NAME = "name";
	private static final String METAMODEL_TITLE = "title";
	private static final String METAMODEL_LENGTH = "length";
	private static final String METAMODEL_DISABLE = "disable";
	private static final String METAMODEL_REQUIRED = "required";
	private static final String METAMODEL_CLASS_FIELD = "ClassField";

	private Class<T> model;
	private Map<String, Map<String, Object>> metamodel;

	/**
	 * @param model
	 * @throws IllegalArgumentException
	 */
	public LayoutMetaModel(Class<T> model) throws IllegalArgumentException {
		if (model == null && !AnnotationUtils.isAnnotationDeclaredLocally(LayoutModel.class, model)) {
			throw new IllegalArgumentException("Argument model must be not null and contain @LayoutModel");
		}
		this.model = model;
		metamodel = new HashMap<>();
		init();
	}

	/**
	 * 
	 */
	private void init() {
		setMetadataFields();
		setMetadataClass();
	}

	/**
	 * 
	 */
	private void setMetadataFields() {
		final Field[] fields = FieldUtils.getFieldsWithAnnotation(model, LayoutField.class);		
		if (fields != null) {
			Map<String, Object> attrs = null;
			String name = StringUtils.EMPTY;
			for (Field field : fields) {
				attrs = AnnotationUtils
						.getAnnotationAttributes(AnnotationUtils.findAnnotation(field, LayoutField.class));
				// If the attribute name is null, the name of the field, into metamodel, must be
				// the field class name
				name = (attrs.get(METAMODEL_NAME) == null) ? field.getName() : attrs.get(METAMODEL_NAME).toString();
				attrs.put(METAMODEL_NAME, field.getName());
				attrs.put(METAMODEL_CLASS_FIELD, field.getName());
				// Add metamodel
				metamodel.put(name, attrs);
				setMetadataWrapperClass(field, name);
				setMetadataConvertClass(field, name);
			}
		}
	}

	/**
	 * 
	 */
	private void setMetadataClass() {
		metamodel.put(model.getName(),
				AnnotationUtils.getAnnotationAttributes(AnnotationUtils.findAnnotation(model, LayoutModel.class)));
	}

	/**
	 * 
	 * @param field
	 * @param name
	 */
	public void setMetadataWrapperClass(final Field field, String name) {
		final LayoutFieldWrapper annotation = AnnotationUtils.findAnnotation(field, LayoutFieldWrapper.class);
		if (annotation != null) {
			metamodel.get(name).putAll(AnnotationUtils.getAnnotationAttributes(annotation));
		}
	}

	/**
	 * @param field
	 * @param name
	 */
	public void setMetadataConvertClass(final Field field, final String name) {
		final LayoutFieldConverter annotation = AnnotationUtils.findAnnotation(field, LayoutFieldConverter.class);
		if (annotation != null) {
			metamodel.get(name).putAll(AnnotationUtils.getAnnotationAttributes(annotation));			
		}
	}

	/**
	 * @return
	 */
	public int getTotalFields() {
		return (this.metamodel == null) ? 0 : this.metamodel.size();
	}

	/**
	 * @return
	 */
	public Set<String> getFieldNames() {
		final Set<String> names = (this.metamodel == null) ? new HashSet<>() : this.metamodel.keySet();
		names.remove(model.getName());
		return names;
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public String getTitle(final String fieldName) {
		return (metamodel.get(fieldName) == null) ? null : metamodel.get(fieldName).get(METAMODEL_TITLE).toString();
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public Integer getLength(final String fieldName) {
		return (metamodel.get(fieldName) == null) ? null : (Integer) metamodel.get(fieldName).get(METAMODEL_LENGTH);
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public boolean isDisabled(final String fieldName) {
		return (metamodel.get(fieldName) == null) ? null : (Boolean) metamodel.get(fieldName).get(METAMODEL_DISABLE);
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public boolean isRequied(final String fieldName) {
		return (metamodel.get(fieldName) == null) ? null : (Boolean) metamodel.get(fieldName).get(METAMODEL_REQUIRED);
	}

	/**
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public StringConverter<?> getConverter(final String fieldName) throws Exception {
		Class<? extends StringConverter<?>> clazz = (metamodel.get(fieldName) == null
				|| metamodel.get(fieldName).get(METAMODEL_CONVERT_CLASS) == null) ? null
						: (Class<? extends StringConverter<?>>) metamodel.get(fieldName).get(METAMODEL_CONVERT_CLASS);
		return (clazz == null) ? null : clazz.newInstance();
	}

	/**
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public Class<?> getWrapperClass(final String fieldName) {
		return (metamodel.get(fieldName) == null || metamodel.get(fieldName).get(METAMODEL_WRAPPER_CLASS) == null)
				? null
				: (Class<?>) metamodel.get(fieldName).get(METAMODEL_WRAPPER_CLASS);
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public String getClassFieldName(final String fieldName) {
		return (metamodel.get(fieldName) == null) ? null
				: metamodel.get(fieldName).get(METAMODEL_CLASS_FIELD).toString();
	}

	/**
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	public LayoutModelValidator<?> getValidator() throws Exception {
		Class<? extends LayoutModelValidator<?>> clazz = (metamodel.get(model.getName()) == null
				|| metamodel.get(model.getName()).get(METAMODEL_VALIDATOR) == null) ? null
						: (Class<? extends LayoutModelValidator<?>>) metamodel.get(model.getName())
								.get(METAMODEL_VALIDATOR);
		return (clazz == null) ? null : clazz.newInstance();
	}

	public Class<T> getModel() {
		return model;
	}
		
}
