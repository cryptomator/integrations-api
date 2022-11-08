package org.cryptomator.integrations.mount;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.net.URI;
import java.nio.file.Path;

public class MountpointTest {

	@Test
	@DisplayName("MountPoint.forPath()")
	@DisabledOnOs(OS.WINDOWS)
	public void testForPath() {
		var path = Path.of("/foo/bar");
		var mountPoint = Mountpoint.forPath(path);

		if (mountPoint instanceof Mountpoint.WithPath m) {
			Assertions.assertEquals(path, m.path());
			Assertions.assertEquals(URI.create("file:///foo/bar"), mountPoint.uri());
		} else {
			Assertions.fail();
		}
	}

	@Test
	@DisplayName("MountPoint.forPath() (Windows)")
	@EnabledOnOs(OS.WINDOWS)
	public void testForPathWindows() {
		var path = Path.of("D:\\foo\\bar");
		var mountPoint = Mountpoint.forPath(path);

		if (mountPoint instanceof Mountpoint.WithPath m) {
			Assertions.assertEquals(path, m.path());
			Assertions.assertEquals(URI.create("file:///D:/foo/bar"), mountPoint.uri());
		} else {
			Assertions.fail();
		}
	}

	@Test
	@DisplayName("MountPoint.forUri()")
	public void testForUri() {
		var uri = URI.create("webdav://localhost:8080/foo/bar");
		var mountPoint = Mountpoint.forUri(uri);

		Assertions.assertTrue(mountPoint instanceof Mountpoint.WithUri);
		Assertions.assertEquals(uri, mountPoint.uri());
	}

}