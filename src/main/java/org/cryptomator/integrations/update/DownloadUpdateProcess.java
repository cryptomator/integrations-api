package org.cryptomator.integrations.update;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public abstract class DownloadUpdateProcess implements UpdateProcess {

	protected final Path workDir;
	private final String downloadFileName;
	private final URI uri;
	private final byte[] checksum;
	private final AtomicLong totalBytes;
	private final LongAdder loadedBytes = new LongAdder();
	private final Thread downloadThread;
	private final CountDownLatch downloadCompleted = new CountDownLatch(1);
	protected volatile IOException downloadException;

	/**
	 * Creates a new DownloadUpdateProcess instance.
	 * @param workDir A temporary directory where to download the update file.
	 * @param downloadFileName The name of the file to which the update will be downloaded
	 * @param uri The URI from which the update will be downloaded.
	 * @param checksum (optional) The expected SHA-256 checksum of the downloaded file, can be null if not required.
	 * @param estDownloadSize The estimated size of the download in bytes.
	 */
	protected DownloadUpdateProcess(Path workDir, String downloadFileName, URI uri, byte[] checksum, long estDownloadSize) {
		this.workDir = workDir;
		this.downloadFileName = downloadFileName;
		this.uri = uri;
		this.checksum = checksum;
		this.totalBytes = new AtomicLong(estDownloadSize);
		this.downloadThread = Thread.ofVirtual().unstarted(this::download);
	}

	protected void startDownload() {
		downloadThread.start();
	}

	@Override
	public double preparationProgress() {
		long total = totalBytes.get();
		if (total <= 0) {
			return -1.0;
		} else {
			return (double) loadedBytes.sum() / totalBytes.get();
		}
	}

	@Override
	public void await() throws InterruptedException {
		downloadCompleted.await();
	}

	@Override
	public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
		return downloadCompleted.await(timeout, unit);
	}

	protected boolean isDone() {
		try {
			return downloadCompleted.await(0, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}

	@Override
	public void cancel() {
		downloadThread.interrupt();
	}

	private void download() {
		try {
			download(workDir.resolve(downloadFileName));
		} catch (IOException e) {
			// TODO: eventually handle this via structured concurrency?
			downloadException = e;
		} finally {
			downloadCompleted.countDown();
		}
	}

	/**
	 * Downloads the update from the given URI and saves it to the specified filename in the working directory.
	 * @param downloadPath The path to where to save the downloaded file.
	 * @throws IOException indicating I/O errors during the download or file writing process or due to checksum mismatch
	 */
	protected void download(Path downloadPath) throws IOException {
		var request = HttpRequest.newBuilder().uri(uri).GET().build();
		try (HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()) { // TODO: make http client injectable?
			// make download request
			var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
			if (response.statusCode() != 200) {
				throw new IOException("Failed to download update, status code: " + response.statusCode());
			}

			// update totalBytes
			response.headers().firstValueAsLong("Content-Length").ifPresent(totalBytes::set);

			// prepare checksum calculation
			MessageDigest sha256;
			try {
				sha256 = MessageDigest.getInstance("SHA-256"); // Initialize SHA-256 digest, not used here but can be extended for checksum validation
			} catch (NoSuchAlgorithmException e) {
				throw new AssertionError("Every implementation of the Java platform is required to support [...] SHA-256", e);
			}

			// write bytes to file
			try (var in = new DownloadInputStream(response.body(), loadedBytes, sha256);
				 var src = Channels.newChannel(in);
				 var dst = FileChannel.open(downloadPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW)) {
				dst.transferFrom(src, 0, Long.MAX_VALUE);
			}

			// verify checksum if provided
			byte[] calculatedChecksum = sha256.digest();
			if (checksum != null && !MessageDigest.isEqual(calculatedChecksum, checksum)) {
				throw new IOException("Checksum verification failed for downloaded file: " + downloadPath);
			}

			// post-download processing
			postDownload(downloadPath);
		} catch (InterruptedException e) {
			throw new InterruptedIOException("Download interrupted");
		}
	}

	protected void postDownload(Path downloadPath) throws IOException {
		// Default implementation does nothing, can be overridden by subclasses for specific post-download actions
	}

	/**
	 * An InputStream decorator that counts the number of bytes read and updates a MessageDigest for checksum calculation.
	 */
	private static class DownloadInputStream extends FilterInputStream {

		private final LongAdder counter;
		private final MessageDigest digest;

		protected DownloadInputStream(InputStream in, LongAdder counter, MessageDigest digest) {
			super(in);
			this.counter = counter;
			this.digest = digest;
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int n = super.read(b, off, len);
			if (n == -1) {
				digest.update(b, off, n);
				counter.add(n);
			}
			return n;
		}

		@Override
		public int read() throws IOException {
			int b = super.read();
			if (b != -1) {
				digest.update((byte) b);
				counter.increment();
			}
			return b;
		}

	}

}
