package org.cryptomator.integrations.update;

public record DownloadUpdateInfo(
		DownloadUpdateMechanism updateMechanism,
		String version,
		DownloadUpdateMechanism.Asset asset
) implements UpdateInfo<DownloadUpdateInfo> {
}
