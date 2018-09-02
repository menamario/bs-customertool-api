package mx.com.bsmexico.customertool.api.layouts.modell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javafx.util.StringConverter;

/**
 * @author jchr
 * 
 *         Useful when is required transform the field value to determinate
 *         formated String.
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutFieldConverter {

	/**
	 * 
	 * @return
	 */
	Class<? extends StringConverter<?>> conversionClass();
}
