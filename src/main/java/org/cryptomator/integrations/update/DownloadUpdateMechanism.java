package org.cryptomator.integrations.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;
import java.util.List;
import java.util.regex.Pattern;

public abstract class DownloadUpdateMechanism implements UpdateMechanism<DownloadUpdateInfo> {

	private static final Logger LOG = LoggerFactory.getLogger(DownloadUpdateMechanism.class);
	private static final String LATEST_VERSION_API_URL = "https://api.cryptomator.org/connect/apps/desktop/latest-version?format=1";
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Pattern SAFE_ASSET_NAME = Pattern.compile("[A-Za-z0-9][A-Za-z0-9._+-]*");

	@Override
	public DownloadUpdateInfo checkForUpdate(String currentVersion, HttpClient httpClient) {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(LATEST_VERSION_API_URL)).build();
			HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
			if (response.statusCode() != 200) {
				LOG.warn("Failed to fetch release: HTTP {}", response.statusCode());
				return null;
			}
			var release = MAPPER.readValue(response.body(), LatestVersionResponse.class);
			return checkForUpdate(currentVersion, release);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOG.debug("Update check interrupted.");
			return null;
		} catch (IOException e) {
			LOG.warn("Update check failed", e);
			return null;
		}
	}

	/**
	 * Returns the first step to prepare the update. This downloads the {@link DownloadUpdateInfo#asset() asset} to a temporary location and verifies its checksum.
	 * @param updateInfo The {@link DownloadUpdateInfo} retrieved from {@link #checkForUpdate(String, HttpClient)}.
	 * @return a new {@link UpdateStep} that can be used to monitor the download progress.
	 * @throws UpdateFailedException When the asset's name is not a plain file name, its digest is malformed, or when failing to prepare a temporary download location.
	 */
	@Override
	public UpdateStep firstStep(DownloadUpdateInfo updateInfo) throws UpdateFailedException {
		var asset = updateInfo.asset();
		// the name is used as file name in the work dir, so it must not be able to point anywhere else
		if (!isSafeAssetName(asset.name())) {
			LOG.debug("Rejected unsafe asset name: \"{}\"", asset.name());
			throw new UpdateFailedException("Asset name is not a safe file name.");
		}
		var digest = parseDigest(asset.digest());
		try {
			Path workDir = Files.createTempDirectory("cryptomator-update");
			return new FirstStep(workDir, updateInfo, digest);
		} catch (IOException e) {
			throw new UpdateFailedException("Failed to create temporary directory for update", e);
		}
	}

	// allowlist of plain file names: no path separators, no drive letters, no leading dot, no shell metacharacters
	static boolean isSafeAssetName(@Nullable String name) {
		return name != null && SAFE_ASSET_NAME.matcher(name).matches();
	}

	// returns the expected SHA-256 checksum or null, if the digest is not in "sha256:<hex>" format
	private static byte @Nullable [] parseDigest(String digest) throws UpdateFailedException {
		if (!digest.startsWith("sha256:")) {
			return null;
		}
		try {
			return HexFormat.of().withLowerCase().parseHex(digest.substring(7)); // remove "sha256:" prefix
		} catch (IllegalArgumentException e) {
			throw new UpdateFailedException("Malformed asset digest.", e);
		}
	}

	/**
	 * Second step that is executed after the download has completed in the {@link #firstStep(DownloadUpdateInfo) first step}.
	 * @param workDir A temporary working directory to which the asset has been downloaded.
	 * @param assetPath The path of the downloaded asset.
	 * @param updateInfo The {@link DownloadUpdateInfo} representing the update.
	 * @return The next step of the update process.
	 * @throws IllegalStateException if preconditions aren't met.
	 * @throws IOException indicating an error preventing the next step from starting.
	 * @implSpec The returned {@link UpdateStep} must either be stateless or a new instance must be returned on each call.
	 */
	public abstract UpdateStep secondStep(Path workDir, Path assetPath, DownloadUpdateInfo updateInfo) throws IllegalStateException, IOException;

	@Nullable
	@Blocking
	protected abstract DownloadUpdateInfo checkForUpdate(String currentVersion, LatestVersionResponse response);

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record LatestVersionResponse(
			@JsonProperty("latestVersion") LatestVersion latestVersion,
			@JsonProperty("assets") List<Asset> assets
	) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record LatestVersion(
			@JsonProperty("mac") String macVersion,
			@JsonProperty("win") String winVersion,
			@JsonProperty("linux") String linuxVersion
	) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Asset(
			@JsonProperty("name") String name,
			@JsonProperty("digest") String digest,
			@JsonProperty("size") long size,
			@JsonProperty("downloadUrl") String downloadUrl
	) {}

	private class FirstStep extends DownloadUpdateStep {
		private final Path workDir;
		private final DownloadUpdateInfo updateInfo;

		public FirstStep(Path workDir, DownloadUpdateInfo updateInfo, byte @Nullable [] digest) {
			var uri = URI.create(updateInfo.asset().downloadUrl);
			var destination = workDir.resolve(updateInfo.asset().name);
			var size = updateInfo.asset().size;
			super(uri, destination, digest, size);
			this.workDir = workDir;
			this.updateInfo = updateInfo;
		}

		@Override
		public @Nullable UpdateStep nextStep() throws IllegalStateException, IOException {
			if (!isDone()) {
				throw new IllegalStateException("Download not yet completed.");
			} else if (downloadException != null) {
				throw new UpdateFailedException("Download failed.", downloadException);
			}
			return secondStep(workDir, destination, updateInfo);
		}
	}

}
