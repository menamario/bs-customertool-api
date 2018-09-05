package mx.com.bsmexico.customertool.api.layouts.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If the Model is LayoutModelType.PROPERTY_JAVABEANS then this annotation
 * indicates what class is wrapping by the Annotated Element that belongs to the
 * package javafx.beans.property
 * 
 * @author jchr
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutFieldWrapper {

	/**
	 * @return
	 */
	Class<? extends Object> wrappedClass();
}
