package mx.com.bsmexico.customertool.api;

import java.util.function.Predicate;

/**
 * @author jchr
 *
 */
public class AlwaysTruePredicate implements Predicate<Object> {

	@Override
	public boolean test(Object t) {		
		return true;
	}

}
