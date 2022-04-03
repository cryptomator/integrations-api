package org.cryptomator.integrations.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

class ClassLoaderFactory {

	private static final String USER_HOME = System.getProperty("user.home");
	private static final String PLUGIN_DIR_KEY = "cryptomator.pluginDir";
	private static final String JAR_SUFFIX = ".jar";

	/**
	 * Attempts to find {@code .jar} files in the path specified in {@value #PLUGIN_DIR_KEY} system property.
	 * A new class loader instance is returned that loads classes from the given classes.
	 *
	 * @return A new URLClassLoader that is aware of all {@code .jar} files in the plugin dir
	 */
	@Contract(value = "-> new", pure = true)
	public static URLClassLoader forPluginDir() {
		String val = System.getProperty(PLUGIN_DIR_KEY, "");
		final Path p;
		if (val.startsWith("~/")) {
			p = Path.of(USER_HOME).resolve(val.substring(2));
		} else {
			p = Path.of(val);
		}
		return forPluginDirWithPath(p);
	}

	@VisibleForTesting
	@Contract(value = "_ -> new", pure = true)
	static URLClassLoader forPluginDirWithPath(Path path) throws UncheckedIOException {
		return URLClassLoader.newInstance(findJars(path));
	}

	@VisibleForTesting
	static URL[] findJars(Path path) {
		try (var stream = Files.walk(path)) {
			return stream.filter(ClassLoaderFactory::isJarFile).map(ClassLoaderFactory::toUrl).toArray(URL[]::new);
		} catch (IOException | UncheckedIOException e) {
			// unable to locate any jars // TODO: log a warning?
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
