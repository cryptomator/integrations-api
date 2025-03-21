package org.cryptomator.integrations.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.stream.Stream;

public class IntegrationsLoader {

	private static final Logger LOG = LoggerFactory.getLogger(IntegrationsLoader.class);

	private IntegrationsLoader() {
	}

	/**
	 * Loads the best suited service provider, i.e. the one with the highest priority that is supported.
	 * <p>
	 * If two services are available with the same priority, it is unspecified which one will be returned.
	 *
	 * @param clazz Service class
	 * @param <T>   Type of the service
	 * @return Highest priority service provider or empty if no supported service provider was found
	 */
	public static <T> Optional<T> load(Class<T> clazz) {
		return loadAll(clazz).findFirst();
	}


	/**
	 * Loads all suited service providers ordered by priority in descending order.
	 * <p>
	 * Only services declared in the `org.cryptomator.integrations.api` module can be loaded with this method.
	 * Foreign services need to use {@link IntegrationsLoader#loadAll(ServiceLoader, Class)}.
	 *
	 * @param clazz Service class
	 * @param <T>   Type of the service
	 * @return An ordered stream of all suited service providers
	 */
	public static <T> Stream<T> loadAll(Class<T> clazz) {
		return loadAll(ServiceLoader.load(clazz, ClassLoaderFactory.forPluginDir()), clazz);
	}

	/**
	 * Loads all suited service providers ordered by priority in descending order.
	 * <p>
	 * This method allows arbitrary services to be loaded, as long as the provided service loader has module access to them.
	 *
	 * @param serviceLoader Loader with own module scope
	 * @param clazz         Service class
	 * @param <T>           Type of the service
	 * @return An ordered stream of all suited service providers
	 */
	public static <T> Stream<T> loadAll(ServiceLoader<T> serviceLoader, @NotNull Class<T> clazz) {
		Objects.requireNonNull(clazz, "Service to load not specified.");
		return serviceLoader.stream()
				.peek(serviceProvider -> logFoundServiceProvider(clazz, serviceProvider.type()))
				.filter(IntegrationsLoader::isSupportedOperatingSystem)
				.filter(IntegrationsLoader::passesStaticAvailabilityCheck)
				.sorted(Comparator.comparingInt(IntegrationsLoader::getPriority).reversed())
				.flatMap(IntegrationsLoader::instantiateServiceProvider)
				.filter(IntegrationsLoader::passesInstanceAvailabilityCheck)
				.peek(impl -> logServiceIsAvailable(clazz, impl.getClass()));
	}

	private static void logFoundServiceProvider(Class<?> apiType, Class<?> implType) {
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
		return silentlyPassesAvailabilityCheck(type, null);
	}

	@VisibleForTesting
	static boolean passesInstanceAvailabilityCheck(Object instance) {
		return silentlyPassesAvailabilityCheck(instance.getClass(), instance);
	}

	private static void logServiceIsAvailable(Class<?> apiType, Class<?> implType) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("{}: Implementation is available: {}", apiType.getSimpleName(), implType.getName());
		}
	}

	private static <T> boolean silentlyPassesAvailabilityCheck(Class<? extends T> type, @Nullable T instance) {
		try {
			return passesAvailabilityCheck(type, instance);
		} catch (ExceptionInInitializerError | NoClassDefFoundError | RuntimeException e) {
			LOG.warn("Unable to load service provider {}.", type.getName(), e);
			return false;
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
