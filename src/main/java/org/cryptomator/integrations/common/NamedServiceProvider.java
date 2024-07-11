package org.cryptomator.integrations.common;

import org.slf4j.LoggerFactory;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A service provider with a human-readable, possibly localized name.
 */
public interface NamedServiceProvider {

	/**
	 * Get the name of this service provider.
	 *
	 * @return The name of the service provider
	 * @implNote The default implementation looks first for a {@link LocalizedDisplayName} and loads the name from the specified resource bundle/key. If the annotation is not present or loading the resource throws an exception, the code looks for {@link DisplayName} and uses its value. If none of the former annotations are present, it falls back to the qualified class name.
	 * @see DisplayName
	 * @see LocalizedDisplayName
	 */
	default String getName() {
		var localizedDisplayName = this.getClass().getAnnotation(LocalizedDisplayName.class);
		if (localizedDisplayName != null) {
			try {
				return ResourceBundle.getBundle(localizedDisplayName.bundle()) //
						.getString(localizedDisplayName.key());
			} catch (MissingResourceException e) {
				var clazz = this.getClass();
				var logger = LoggerFactory.getLogger(clazz);
				logger.warn("Failed to load localized display name for {}. Falling back to not-localized display name/class name.", clazz.getName(), e);
			}
		}

		var displayName = this.getClass().getAnnotation(DisplayName.class);
		if (displayName != null) {
			return displayName.value();
		} else {
			return this.getClass().getName();
		}
	}
}
