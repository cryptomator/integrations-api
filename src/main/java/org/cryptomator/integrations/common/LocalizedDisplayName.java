package org.cryptomator.integrations.common;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A humanreadable, localized name of the annotated class.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ApiStatus.Experimental
public @interface LocalizedDisplayName {

	/**
	 * Name of the localization bundle, where the display name is loaded from.
	 *
	 * @return Name of the localization bundle
	 */
	String bundle();

	/**
	 * The localization key containing the display name.
	 *
	 * @return Localization key to use
	 */
	String key();

}
