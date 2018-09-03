package mx.com.bsmexico.customertool.api.layouts.modell;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.annotation.AnnotationUtils;

import javafx.util.StringConverter;

/**
 * @author jchr
 *
 */
public class LayoutMetaModel<T> {
	private static final String METAMODEL_RESTRICTION = "RESTRICTION";
	private static final String METAMODEL_RESTRICTION_DESC = "RESTRICTION_DESC";
	private static final String METAMODEL_WRAPPER_CLASS = "WRAPPER_CLASS";
	private static final String METAMODEL_CONVERT_CLASS = "CONVERT_CLASS";
	private static final String METAMODEL_NAME = "name";
	private static final String METAMODEL_TITLE = "title";
	private static final String METAMODEL_LENGTH = "length";
	private static final String METAMODEL_DISABLE = "disable";
	private static final String METAMODEL_REQUIRED = "required";
	private static final String METAMODEL_CLASS_FIELD = "CLASS_FIELD";

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
		setMetadataRestrictions();
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
	 * @param field
	 * @param name
	 */
	public void setMetadataWrapperClass(final Field field, String name) {
		final LayoutFieldWrapper annotation = AnnotationUtils.findAnnotation(field, LayoutFieldWrapper.class);
		if (annotation != null) {
			metamodel.get(name).put(METAMODEL_WRAPPER_CLASS, annotation.wrappedClass());
		}
	}

	/**
	 * @param field
	 * @param name
	 */
	public void setMetadataConvertClass(final Field field, final String name) {
		final LayoutFieldConverter annotation = AnnotationUtils.findAnnotation(field, LayoutFieldConverter.class);
		if (annotation != null) {
			metamodel.get(name).put(METAMODEL_CONVERT_CLASS, annotation.conversionClass());
		}
	}

	/**
	 * 
	 */
	private void setMetadataRestrictions() {
		final List<Object> elems = new ArrayList<>();
		elems.addAll(FieldUtils.getFieldsListWithAnnotation(model, RestrictionLayoutField.class));
		elems.addAll(MethodUtils.getMethodsListWithAnnotation(model, RestrictionLayoutField.class));
		if (!elems.isEmpty()) {
			Map<String, Object> attrs = null;
			for (Object elem : elems) {
				if (elem instanceof Field) {
					if (!((Field) elem).getType().getName().equals("java.util.function.Predicate")) {
						continue;
					}
				}
				if (elem instanceof Method) {
					if (!((Method) elem).getReturnType().getName().equals("java.util.function.Predicate")) {
						continue;
					}
				}
				attrs = AnnotationUtils.getAnnotationAttributes(
						AnnotationUtils.findAnnotation((AnnotatedElement) elem, RestrictionLayoutField.class));
				if (attrs != null) {
					String[] classFields = (String[]) attrs.get("fields");
					String desc = (String) attrs.get("description");
					for (String cf : classFields) {
						if (metamodel.get(cf) != null) {
							metamodel.get(cf).put(METAMODEL_RESTRICTION, elem);
							metamodel.get(cf).put(METAMODEL_RESTRICTION_DESC, desc);
						}
					}
				}
			}
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
		return (this.metamodel == null) ? new HashSet<>() : this.metamodel.keySet();
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
	 */
	@SuppressWarnings("rawtypes")
	public Predicate getRestriction(final String fieldName) throws Exception{
		Predicate<?> result = null;
		if(metamodel.get(fieldName) != null || metamodel.get(fieldName).get(METAMODEL_RESTRICTION) != null){
			Object obj = metamodel.get(fieldName).get(METAMODEL_RESTRICTION);
			if(obj instanceof Field) {
				((Field)obj).setAccessible(true);
				result = (Predicate<?>) ((Field)obj).get(null);
			}
		}
		return result;
	}
}
