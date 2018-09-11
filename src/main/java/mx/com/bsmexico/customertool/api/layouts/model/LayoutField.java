package mx.com.bsmexico.customertool.api.layouts.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jchr
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutField {

	String name() default StringUtils.EMPTY;

	/**
	 * The meaning of this attribute depends on the context in which it is used.
	 * 
	 * Field Name.
	 * 
	 * @return
	 */
	String title() default StringUtils.EMPTY;

	/**
	 * The meaning of this attribute depends on the context in which it is used.
	 * 
	 * @return
	 */
	int length();

	/**
	 * The meaning of this attribute depends on the context in which it is used
	 * 
	 * @return
	 */
	boolean disable() default false;

	/**
	 * The meaning of this attribute depends on the context in which it is used
	 * 
	 * @return
	 */
	boolean required() default true;

	/**
	 * 
	 * The meaning of this attribute depends on the context in which it is used
	 * 
	 * @return
	 */
	boolean hidden() default false;

	/**
	 * 
	 * The meaning of this attribute depends on the context in which it is used
	 * 
	 * @return
	 */
	boolean editable() default true;
}
