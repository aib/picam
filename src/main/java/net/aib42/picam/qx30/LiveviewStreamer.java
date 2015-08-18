package net.aib42.picam.qx30;
import java.io.IOException;
import java.io.InputStream;

import net.aib42.util.bits.NetworkByteOrder;
import net.aib42.util.stream.InputStreamBlockingReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class LiveviewStreamer {
	private HttpClient httpClient;
	private HttpGet currentRequest;
	private InputStreamBlockingReader isReader;

	private Long firstFrameTimestamp;
	private Long firstFrameTime;

	public static class JpegPayload {
		public long timestamp;
		public byte[] bytes;

		public JpegPayload(long timestamp, byte[] bytes) {
			this.timestamp = timestamp;
			this.bytes = bytes;
		}
	}

	public LiveviewStreamer() {
		httpClient = HttpClients.createDefault();
		currentRequest = null;
	}

	public void start(String url) throws IllegalStateException, IOException {
		if (currentRequest != null) {
			throw new IllegalStateException("Already started");
		}

		currentRequest = new HttpGet(url + "/liveview/liveviewstream");
		HttpResponse response = httpClient.execute(currentRequest);
		InputStream responseInputStream = response.getEntity().getContent();
		isReader = new InputStreamBlockingReader(responseInputStream);
		firstFrameTimestamp = null;
		firstFrameTime = null;
	}

	public void stop() {
		if (currentRequest == null) {
			return;
		}

		currentRequest.abort();
		currentRequest = null;
	}

	public JpegPayload getNextJpeg() throws IOException {
		byte[] header = new byte[12];
		isReader.read(header);

		long timestamp = NetworkByteOrder.bytesToLong(
			header[0], header[1], header[2], header[3], header[4], header[5], header[6], header[7]);
		int dataSize = NetworkByteOrder.bytesToInt(header[8], header[9], header[10], header[11]);

		byte[] data = new byte[dataSize];
		isReader.read(data);

		if (firstFrameTimestamp == null || firstFrameTime == null) {
			firstFrameTimestamp = timestamp;
			firstFrameTime = System.currentTimeMillis();
		}

		return new JpegPayload(timestamp, data);
	}

	public Long getLag(JpegPayload payload) {
		if (firstFrameTimestamp == null || firstFrameTime == null) {
			return null;
		}

		long deltaTime = System.currentTimeMillis() - firstFrameTime;
		long deltaTimestamp = payload.timestamp - firstFrameTimestamp;
		return deltaTime - deltaTimestamp;
	}
}
