package org.cryptomator.integrations.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jetbrains.annotations.ApiStatus;

/**
 * A humanreadable name of the annotated class.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ApiStatus.Experimental
public @interface DisplayName {
	String value();
    
}
