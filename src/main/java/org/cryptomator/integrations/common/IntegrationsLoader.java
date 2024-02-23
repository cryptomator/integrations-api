package org.cryptomator.integrations.common;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.stream.Stream;

public class IntegrationsLoader {

	private static final Logger LOG = LoggerFactory.getLogger(IntegrationsLoader.class);

	private IntegrationsLoader() {
	}

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
				.peek(service -> logFoundService(clazz, service.type()))
				.filter(IntegrationsLoader::isSupportedOperatingSystem)
				.filter(IntegrationsLoader::passesStaticAvailabilityCheck)
				.sorted(Comparator.comparingInt(IntegrationsLoader::getPriority).reversed())
				.flatMap(IntegrationsLoader::instantiateServiceProvider)
				.filter(IntegrationsLoader::passesInstanceAvailabilityCheck)
				.peek(impl -> logServiceIsAvailable(clazz, impl.getClass()));
	}

	private static void logFoundService(Class<?> apiType, Class<?> implType) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("{}: Found implementation: {} in jar {}", apiType.getSimpleName(), implType.getName(), implType.getProtectionDomain().getCodeSource().getLocation().getPath());
		}
	}

	private static int getPriority(ServiceLoader.Provider<?> provider) {
		var prio = provider.type().getAnnotation(Priority.class);
		return prio == null ? Priority.DEFAULT : prio.value();
	}

	private static boolean isSupportedOperatingSystem(ServiceLoader.Provider<?> provider) {
		var annotations = provider.type().getAnnotationsByType(OperatingSystem.class);
		return annotations.length == 0 || Arrays.stream(annotations).anyMatch(OperatingSystem.Value::isCurrent);
	}

	private static <T> Stream<T> instantiateServiceProvider(ServiceLoader.Provider<T> provider) {
		try {
			return Stream.of(provider.get());
		} catch (ServiceConfigurationError err) {
			//ServiceLoader.Provider::get throws this error if (from javadoc)
			// * the public static "provider()" method of a provider factory returns null
			// * the service provider cannot be instantiated due to an error/throw
			LOG.warn("Unable to load service provider {}.", provider.type().getName(), err);
			return Stream.empty();
		}
	}

	private static boolean passesStaticAvailabilityCheck(ServiceLoader.Provider<?> provider) {
		return passesStaticAvailabilityCheck(provider.type());
	}

	@VisibleForTesting
	static boolean passesStaticAvailabilityCheck(Class<?> type) {
		try {
			return passesAvailabilityCheck(type, null);
		} catch (ExceptionInInitializerError | NoClassDefFoundError | RuntimeException t) {
			LOG.warn("Unable to load service provider {}.", type.getName(), t);
			return false;
		}
	}

	@VisibleForTesting
	static boolean passesInstanceAvailabilityCheck(Object instance) {
		try {
			return passesAvailabilityCheck(instance.getClass(), instance);
		} catch (RuntimeException rte) {
			LOG.warn("Unable to load service provider {}.", instance.getClass().getName(), rte);
			return false;
		}
	}

	private static void logServiceIsAvailable(Class<?> apiType, Class<?> implType) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("{}: Implementation is available: {}", apiType.getSimpleName(), implType.getName());
		}
	}

	private static <T> boolean passesAvailabilityCheck(Class<? extends T> type, @Nullable T instance) {
		if (!type.isAnnotationPresent(CheckAvailability.class)) {
			return true; // if type is not annotated, skip tests
		}
		if (!type.getModule().isExported(type.getPackageName(), IntegrationsLoader.class.getModule())) {
			LOG.error("Can't run @CheckAvailability tests for class {}. Make sure to export {} to {}!", type.getName(), type.getPackageName(), IntegrationsLoader.class.getPackageName());
			return false;
		}
		return Arrays.stream(type.getMethods())
				.filter(m -> isAvailabilityCheck(m, instance == null))
				.allMatch(m -> passesAvailabilityCheck(m, instance));
	}

	private static boolean passesAvailabilityCheck(Method m, @Nullable Object instance) {
		assert Boolean.TYPE.equals(m.getReturnType());
		try {
			return (boolean) m.invoke(instance);
		} catch (ReflectiveOperationException e) {
			LOG.warn("Failed to invoke @CheckAvailability test {}#{}", m.getDeclaringClass(), m.getName(), e);
			return false;
		}
	}

	private static boolean isAvailabilityCheck(Method m, boolean isStatic) {
		return m.isAnnotationPresent(CheckAvailability.class)
				&& Boolean.TYPE.equals(m.getReturnType())
				&& m.getParameterCount() == 0
				&& Modifier.isStatic(m.getModifiers()) == isStatic;
	}

}
