package mx.com.bsmexico.customertool.api.layouts.modell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author jchr
 *
 */
@Target({ElementType.FIELD/*, ElementType.METHOD*/})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestrictionLayoutField {

	/**
	 * 
	 * Description of the restriction
	 * 
	 * @return
	 */
	String description();

	/**
	 * Collection of names of class fields, annotated with {@link LayoutField}, to
	 * apply the restriction
	 * 
	 * @return
	 */
	String[] fields();

}
