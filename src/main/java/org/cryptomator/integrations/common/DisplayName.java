package org.cryptomator.integrations.common;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A humanreadable name of the annotated class.
 * <p>
 * Checked in the default implementation of the {@link NamedServiceProvider#getName()} with lower priority.
 *
 * @see NamedServiceProvider
 * @see LocalizedDisplayName
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ApiStatus.Experimental
public @interface DisplayName {
	String value();
}
