package org.cryptomator.integrations.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Integration Priority.
 * <p>
 * If multiple implementations for an integration can be provided, the provider with the highest priority will be used.
 *
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Priority {
	int DEFAULT = 0;
	int FALLBACK = Integer.MIN_VALUE;

	int value() default DEFAULT;
}
