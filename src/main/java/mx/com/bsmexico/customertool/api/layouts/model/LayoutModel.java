package mx.com.bsmexico.customertool.api.layouts.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mx.com.bsmexico.customertool.api.layouts.model.validation.AlwaysTrueLayoutModelValidator;
import mx.com.bsmexico.customertool.api.layouts.model.validation.LayoutModelValidator;

/**
 * @author jchr
 * 
 *         All classes that represents a layout model must be annotated with
 *         this metadata
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutModel {
	LayoutModelType type();

	Class<? extends LayoutModelValidator<?>> validatorClass() default AlwaysTrueLayoutModelValidator.class;
}
