package org.cryptomator.integrations.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadUpdateMechanismTest {

	private final DownloadUpdateMechanism mechanism = new DownloadUpdateMechanism() {
		@Override
		public UpdateStep secondStep(Path workDir, Path assetPath, DownloadUpdateInfo updateInfo) {
			throw new UnsupportedOperationException();
		}

		@Override
		protected DownloadUpdateInfo checkForUpdate(String currentVersion, LatestVersionResponse response) {
			throw new UnsupportedOperationException();
		}
	};

	@ParameterizedTest
	@ValueSource(strings = {
			"Cryptomator-1.18.0-x64.msi",
			"Cryptomator-1.18.0-x64.exe",
			"Cryptomator-1.18.0-arm64.dmg",
			"cryptomator-1.18.0-x86_64.AppImage",
			"Cryptomator-1.18.0-beta1+build.42.dmg",
			"a"
	})
	public void testSafeAssetNames(String name) {
		Assertions.assertTrue(DownloadUpdateMechanism.isSafeAssetName(name));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {
			".",
			"..",
			".hidden.exe",
			"-rf.exe",
			"../Cryptomator-x64.exe",
			"..\\..\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Cryptomator-x64.exe",
			"sub/Cryptomator-x64.exe",
			"/etc/Cryptomator.AppImage",
			"C:\\Cryptomator-x64.exe",
			"C:Cryptomator-x64.exe",
			"Cryptomator-x64.exe:stream",
			"Cryptomator x64.exe",
			"Cryptomator\t.exe",
			"Cryptomator\n.exe",
			"Cryptomator\u0000.exe",
			"Cryptomator&calc.exe",
			"Cryptomator%TEMP%.exe",
			"Cryptomator$(id).AppImage",
			"Cryptomator\".exe",
			"Cryptomätor.exe"
	})
	public void testUnsafeAssetNames(String name) {
		Assertions.assertFalse(DownloadUpdateMechanism.isSafeAssetName(name));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {
			"..\\..\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Cryptomator-x64.exe",
			"../../.local/share/applications/cryptomator.desktop"
	})
	public void testFirstStepRejectsUnsafeAssetName(String name) {
		var updateInfo = updateInfo(name, "sha256:" + "00".repeat(32));

		Assertions.assertThrows(UpdateFailedException.class, () -> mechanism.firstStep(updateInfo));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"sha256:0",
			"sha256:xyz",
			"sha256:-0123456789abcdef"
	})
	public void testFirstStepRejectsMalformedDigest(String digest) {
		var updateInfo = updateInfo("Cryptomator-1.18.0-x64.msi", digest);

		Assertions.assertThrows(UpdateFailedException.class, () -> mechanism.firstStep(updateInfo));
	}

	@Test
	public void testFirstStepAcceptsSafeAssetName() throws IOException {
		var updateInfo = updateInfo("Cryptomator-1.18.0-x64.msi", "sha256:" + "00".repeat(32));

		var step = Assertions.assertInstanceOf(DownloadUpdateStep.class, mechanism.firstStep(updateInfo));
		var workDir = step.destination.getParent();
		try {
			Assertions.assertEquals("Cryptomator-1.18.0-x64.msi", step.destination.getFileName().toString());
			Assertions.assertTrue(Files.isDirectory(workDir));
		} finally {
			Files.deleteIfExists(workDir);
		}
	}

	private DownloadUpdateInfo updateInfo(String assetName, String digest) {
		var asset = new DownloadUpdateMechanism.Asset(assetName, digest, 42L, "https://example.com/" + assetName);
		return new DownloadUpdateInfo(mechanism, "1.18.0", asset);
	}

}
