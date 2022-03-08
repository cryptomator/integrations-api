package org.cryptomator.integrations.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarBuilder implements AutoCloseable {

	private final Manifest manifest = new Manifest();
	private final JarOutputStream jos;

	public JarBuilder(JarOutputStream jos) {
		this.jos = jos;
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
	}

	public static JarBuilder withTarget(OutputStream out) throws IOException {
		return new JarBuilder(new JarOutputStream(out));
	}

	public void addFile(String path, InputStream content) throws IOException {
		jos.putNextEntry(new JarEntry(path));
		content.transferTo(jos);
		jos.closeEntry();
	}

	@Override
	public void close() throws IOException {
		jos.putNextEntry(new JarEntry(JarFile.MANIFEST_NAME));
		manifest.write(jos);
		jos.closeEntry();
		jos.close();
	}
}
