package org.cryptomator.integrations.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;

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
			"..",
			"-rf.exe",
			"../Cryptomator-x64.exe",
			"..\\..\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Cryptomator-x64.exe",
			"C:Cryptomator-x64.exe",
			"Cryptomator x64.exe",
			"Cryptomator\u0000.exe",
			"Cryptomator&calc.exe",
			"Cryptomätor.exe"
	})
	public void testUnsafeAssetNames(String name) {
		Assertions.assertFalse(DownloadUpdateMechanism.isSafeAssetName(name));
	}

	@Test
	public void testFirstStepRejectsUnsafeAssetName() {
		var updateInfo = updateInfo("..\\..\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Cryptomator-x64.exe", "sha256:" + "00".repeat(32));

		Assertions.assertThrows(UpdateFailedException.class, () -> mechanism.firstStep(updateInfo));
	}

	@ParameterizedTest
	@ValueSource(strings = {"0123456789abcdef", "0123456789ABCDEF"})
	public void testParseSha256Digest(String hexBlock) throws UpdateFailedException {
		var digest = DownloadUpdateMechanism.parseSha256Digest("sha256:" + hexBlock.repeat(4));

		Assertions.assertArrayEquals(HexFormat.of().parseHex("0123456789abcdef".repeat(4)), digest);
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"",
			"md5:0123456789abcdef0123456789abcdef",
			"sha512:0123456789abcdef"
	})
	public void testParseSha256DigestIgnoresOtherAlgorithms(String digest) throws UpdateFailedException {
		Assertions.assertNull(DownloadUpdateMechanism.parseSha256Digest(digest));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"sha256:000000000000000000000000000000000000000000000000000000000000000", // 63 chars
			"sha256:00000000000000000000000000000000000000000000000000000000000000000" // 65 chars
	})
	public void testParseSha256DigestRejectsWrongLength(String digest) {
		var e = Assertions.assertThrows(UpdateFailedException.class, () -> DownloadUpdateMechanism.parseSha256Digest(digest));

		Assertions.assertNull(e.getCause()); // rejected by the length check, not by the hex parser
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"sha256:000000000000000000000000000000000000000000000000000000000000000g",
			"sha256:000000000000000000000000000000000000000000000000000000000000000０" // fullwidth digit zero, accepted by Character.digit()
	})
	public void testParseSha256DigestRejectsNonHex(String digest) {
		var e = Assertions.assertThrows(UpdateFailedException.class, () -> DownloadUpdateMechanism.parseSha256Digest(digest));

		Assertions.assertInstanceOf(IllegalArgumentException.class, e.getCause()); // passed the length check, rejected by the hex parser
	}

	@Test
	public void testFirstStepRejectsMalformedDigest() {
		var updateInfo = updateInfo("Cryptomator-1.18.0-x64.msi", "sha256:0");

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
