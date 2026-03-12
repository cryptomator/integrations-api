package org.cryptomator.integrations.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

public class ClassLoaderFactoryTest {

	@Nested
	@DisplayName("When two .jars exist in the plugin dir")
	public class WithJars {

		private static final byte[] FOO_CONTENTS = "foo = 42".getBytes();
		private static final byte[] BAR_CONTENTS = "bar = 23".getBytes();
		private Path pluginDir;

		@BeforeEach
		public void setup(@TempDir Path tmpDir) throws IOException {
			Files.createDirectory(tmpDir.resolve("plugin1"));
			try (var out = Files.newOutputStream(tmpDir.resolve("plugin1/foo.jar"));
				 var jar = JarBuilder.withTarget(out)) {
				jar.addFile("foo.properties", new ByteArrayInputStream(FOO_CONTENTS));
			}

			Files.createDirectory(tmpDir.resolve("plugin2"));
			try (var out = Files.newOutputStream(tmpDir.resolve("plugin2/bar.jar"));
				 var jar = JarBuilder.withTarget(out)) {
				jar.addFile("bar.properties", new ByteArrayInputStream(BAR_CONTENTS));
			}

			this.pluginDir = tmpDir;
		}

		@Test
		@DisplayName("can load resources from both jars")
		public void testForPluginDirWithPath() throws IOException {
			try (var cl = ClassLoaderFactory.forPluginDirWithPath(pluginDir);
				 var fooIn = cl.getResourceAsStream("foo.properties");
				 var barIn = cl.getResourceAsStream("bar.properties")) {
				var fooContents = fooIn.readAllBytes();
				var barContents = barIn.readAllBytes();

				Assertions.assertArrayEquals(FOO_CONTENTS, fooContents);
				Assertions.assertArrayEquals(BAR_CONTENTS, barContents);
			}
		}

		@Test
		@DisplayName("can load resources when path is set in cryptomator.pluginDir")
		public void testForPluginDirFromSysProp() throws IOException {
			System.setProperty("cryptomator.pluginDir", pluginDir.toString());

			try (var cl = ClassLoaderFactory.forPluginDir();
				 var fooIn = cl.getResourceAsStream("foo.properties");
				 var barIn = cl.getResourceAsStream("bar.properties")) {
				var fooContents = fooIn.readAllBytes();
				var barContents = barIn.readAllBytes();

				Assertions.assertArrayEquals(FOO_CONTENTS, fooContents);
				Assertions.assertArrayEquals(BAR_CONTENTS, barContents);
			}
		}
	}

	@Test
	@DisplayName("read path from cryptomator.pluginDir")
	public void testReadPluginDirFromSysProp() {
		var ucl = Mockito.mock(URLClassLoader.class, "ucl");
		var absPath = "/there/will/be/plugins";
		try (var mockedClass = Mockito.mockStatic(ClassLoaderFactory.class)) {
			mockedClass.when(() -> ClassLoaderFactory.forPluginDir()).thenCallRealMethod();
			mockedClass.when(() -> ClassLoaderFactory.forPluginDirWithPath(Path.of(absPath))).thenReturn(ucl);

			System.setProperty("cryptomator.pluginDir", absPath);
			var result = ClassLoaderFactory.forPluginDir();

			Assertions.assertSame(ucl, result);
		}
	}

	@Nested
	@DisplayName("when the system property contains invalid values")
	public class InvalidSystemProperty {

		MockedStatic<ClassLoaderFactory> mockedClass;

		@BeforeEach
		public void beforeEach() {
			mockedClass = Mockito.mockStatic(ClassLoaderFactory.class);
			mockedClass.when(() -> ClassLoaderFactory.forPluginDir()).thenCallRealMethod();
			mockedClass.when(() -> ClassLoaderFactory.forPluginDirWithPath(any())).thenReturn(null);
		}

		@AfterEach
		public void afterEach() {
			mockedClass.close();
		}


		@Test
		@DisplayName("Undefined cryptomator.pluginDir returns empty URLClassLoader")
		public void testUndefinedSysProp() {
			System.clearProperty("cryptomator.pluginDir");
			var result = ClassLoaderFactory.forPluginDir();

			mockedClass.verify(() -> ClassLoaderFactory.forPluginDirWithPath(any()), never());
			Assertions.assertNotNull(result);
			Assertions.assertEquals(0, result.getURLs().length);
		}

		@ParameterizedTest
		@DisplayName("Property cryptomator.pluginDir filled with blanks returns empty URLClassLoader")
		@EmptySource
		@ValueSource(strings = {"\t\t", "  "})
		public void testBlankSysProp(String propValue) {
			System.setProperty("cryptomator.pluginDir", propValue);
			var result = ClassLoaderFactory.forPluginDir();

			mockedClass.verify(() -> ClassLoaderFactory.forPluginDirWithPath(any()), never());
			Assertions.assertNotNull(result);
			Assertions.assertEquals(0, result.getURLs().length);
		}

	}

	@Test
	@DisplayName("findJars returns empty list if not containing jars")
	public void testFindJars1(@TempDir Path tmpDir) throws IOException {
		Files.createDirectories(tmpDir.resolve("dir1"));
		Files.createFile(tmpDir.resolve("file1"));

		var urls = ClassLoaderFactory.findJars(tmpDir);

		Assertions.assertArrayEquals(new URL[0], urls);
	}

	@Test
	@DisplayName("findJars returns urls of found jars")
	public void testFindJars2(@TempDir Path tmpDir) throws IOException {
		Files.createDirectories(tmpDir.resolve("dir1"));
		Files.createDirectories(tmpDir.resolve("dir2"));
		Files.createDirectories(tmpDir.resolve("dir1").resolve("dir2"));
		Files.createFile(tmpDir.resolve("a.jar"));
		Files.createFile(tmpDir.resolve("a.txt"));
		Files.createFile(tmpDir.resolve("dir2").resolve("b.jar"));

		var urls = ClassLoaderFactory.findJars(tmpDir);

		Arrays.sort(urls, Comparator.comparing(URL::toString));
		Assertions.assertArrayEquals(new URL[] {
				new URL(tmpDir.toUri() + "a.jar"),
				new URL(tmpDir.toUri() + "dir2/b.jar")
		}, urls);
	}

}