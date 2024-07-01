package org.cryptomator.integrations.common;

/**
 * A service provider with a specific, human-readable name.
 * 
 */
public interface NamedServiceProvider {
    
    /**
     * Get the name of this service provider.
     * @implNote The default implementation looks for the {@link DisplayName} annotation and uses its value. If the annotation is not present, it falls back to the qualified class name.
     * @return The name of the service provider
     * 
     * @see DisplayName
     */
    default public String getName() {
        var displayName = this.getClass().getAnnotation(DisplayName.class);
        if(displayName != null) {
            return displayName.value();
        } else {
            return this.getClass().getName();
        }
    }
}
