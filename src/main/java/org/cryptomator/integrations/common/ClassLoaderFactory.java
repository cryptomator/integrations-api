package org.cryptomator.integrations.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

class ClassLoaderFactory {

	private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderFactory.class);
	private static final String PLUGIN_DIR_KEY = "cryptomator.pluginDir";
	private static final String JAR_SUFFIX = ".jar";
	private static final AtomicReference<URLClassLoader> CLASSLOADER_CACHE = new AtomicReference<>(null);

	/**
	 * Attempts to find {@code .jar} files in the path specified in {@value #PLUGIN_DIR_KEY} system property.
	 * Returns the cached class loader instance. If no instance is cached, creates a new instance with {@link #forPluginDir()} and caches it.
	 *
	 * @return A URLClassLoader that is aware of all {@code .jar} files in the plugin dir
	 */
	@Contract(value = "-> _", pure = false)
	public synchronized static URLClassLoader forPluginDirCached() {
		var ucl = CLASSLOADER_CACHE.get();
		if (ucl == null) {
			ucl = forPluginDir();
			CLASSLOADER_CACHE.set(ucl);
		}
		return ucl;
	}

	/**
	 * Attempts to find {@code .jar} files in the path specified in {@value #PLUGIN_DIR_KEY} system property.
	 * A new class loader instance is returned that loads classes from the given classes.
	 *
	 * @return A new URLClassLoader that is aware of all {@code .jar} files in the plugin dir
	 */
	@Contract(value = "-> new", pure = true)
	public static URLClassLoader forPluginDir() {
		String val = System.getProperty(PLUGIN_DIR_KEY);
		if (val == null) {
			return URLClassLoader.newInstance(new URL[0]);
		}

		try {
			if (val.isBlank()) {
				throw new IllegalArgumentException("Plugin dir path is blank");
			}
			return forPluginDirWithPath(Path.of(val)); //Path.of might throw InvalidPathException
		} catch (IllegalArgumentException e) {
			LOG.debug("{} contains illegal value. Skipping plugin directory.", PLUGIN_DIR_KEY, e);
			return URLClassLoader.newInstance(new URL[0]);
		}
	}

	@VisibleForTesting
	@Contract(value = "_ -> new", pure = true)
	static URLClassLoader forPluginDirWithPath(Path path) throws UncheckedIOException {
		var jars = findJars(path);
		if (LOG.isDebugEnabled() && jars.length != 0) {
			String jarList = Arrays.stream(jars).map(URL::getPath).collect(Collectors.joining(", "));
			LOG.debug("Found jars in cryptomator.pluginDir: {}", jarList);
		}
		return URLClassLoader.newInstance(jars);
	}

	@VisibleForTesting
	static URL[] findJars(Path path) {
		try (var stream = Files.walk(path)) {
			return stream.filter(ClassLoaderFactory::isJarFile).map(ClassLoaderFactory::toUrl).toArray(URL[]::new);
		} catch (IOException | UncheckedIOException e) {
			LOG.debug("Failed to read plugin dir {}", path, e);
			return new URL[0];
		}
	}

	private static URL toUrl(Path path) throws UncheckedIOException {
		try {
			return path.toUri().toURL();
		} catch (MalformedURLException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static boolean isJarFile(Path path) {
		return Files.isRegularFile(path) && path.getFileName().toString().toLowerCase().endsWith(JAR_SUFFIX);
	}

}
