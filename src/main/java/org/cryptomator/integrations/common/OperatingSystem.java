package org.cryptomator.integrations.common;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restricts the annotated integration provider to one or more operating system(s).
 *
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Repeatable(OperatingSystem.OperatingSystems.class)
@ApiStatus.Experimental
public @interface OperatingSystem {
	Value value() default Value.UNKNOWN;

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@interface OperatingSystems {
		OperatingSystem[] value();
	}

	enum Value {
		LINUX,
		MAC,
		WINDOWS,
		UNKNOWN;

		private static final String OS_NAME = System.getProperty("os.name", "").toLowerCase();

		public static Value current() {
			if (OS_NAME.contains("linux")) {
				return LINUX;
			} else if (OS_NAME.contains("mac")) {
				return MAC;
			} else if (OS_NAME.contains("windows")) {
				return WINDOWS;
			} else {
				return UNKNOWN;
			}
		}

		public static boolean isCurrent(OperatingSystem os) {
			return current().equals(os.value());
		}
	}
}
