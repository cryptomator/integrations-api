package org.cryptomator.integrations.common;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Stream;

public class IntegrationsLoader {

	private IntegrationsLoader(){}

	/**
	 * Loads the best suited service, i.e. the one with the highest priority that is supported.
	 * <p>
	 * If two services are available with the same priority, it is unspecified which one will be returned.
	 *
	 * @param clazz Service class
	 * @param <T>   Type of the service
	 * @return Highest priority service or empty if no supported service was found
	 */
	public static <T> Optional<T> load(Class<T> clazz) {
		return loadAll(clazz).findFirst();
	}

	/**
	 * Loads all suited services ordered by priority in descending order.
	 *
	 * @param clazz Service class
	 * @param <T>   Type of the service
	 * @return An ordered stream of all suited service candidates
	 */
	public static <T> Stream<T> loadAll(Class<T> clazz) {
		return ServiceLoader.load(clazz, ClassLoaderFactory.forPluginDir())
				.stream()
				.filter(IntegrationsLoader::isSupportedOperatingSystem)
				.sorted(Comparator.comparingInt(IntegrationsLoader::getPriority).reversed())
				.map(ServiceLoader.Provider::get);
	}

	private static int getPriority(ServiceLoader.Provider<?> provider) {
		var prio = provider.type().getAnnotation(Priority.class);
		return prio == null ? Priority.DEFAULT : prio.value();
	}

	private static boolean isSupportedOperatingSystem(ServiceLoader.Provider<?> provider) {
		var annotations = provider.type().getAnnotationsByType(OperatingSystem.class);
		return annotations.length == 0 || Arrays.stream(annotations).anyMatch(OperatingSystem.Value::isCurrent);
	}

}
