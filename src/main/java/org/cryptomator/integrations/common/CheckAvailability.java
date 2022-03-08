package org.cryptomator.integrations.common;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies 0..n public methods to check preconditions for the integration to work. These are the rules:
 *
 * <ul>
 *     <li>Both the type and the method(s) must be annotated with {@code @CheckAvailability}</li>
 *     <li>Only public no-arg boolean methods are considered</li>
 *     <li>Methods <em>may</em> be {@code static}, in which case they get invoked before instantiating the service</li>
 *     <li>Should the method throw an exception, it has the same effect as returning {@code false}</li>
 *     <li>No specific execution order is guaranteed in case of multiple annotated methods</li>
 *     <li>Annotations must be present on classes or ancestor classes, not on interfaces</li>
 * </ul>
 *
 * Example:
 * <pre>
 * {@code
 * @CheckAvailability
 * public class Foo {
 *	@CheckAvailability
 *	public static boolean isSupported() {
 *		return "enabled".equals(System.getProperty("plugin.status"));
 *	}
 * }
 * }
 * </pre>
 * <p>
 * Annotations are discovered at runtime using reflection, so make sure to make relevant classes accessible to this
 * module ({@code opens X to org.cryptomator.integrations.api}).
 *
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@ApiStatus.Experimental
public @interface CheckAvailability {
}
