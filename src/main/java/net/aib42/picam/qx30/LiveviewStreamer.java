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
	}

	public void stop() {
		if (currentRequest == null) {
			return;
		}

		currentRequest.abort();
		currentRequest = null;
	}

	public byte[] getNextJpegBytes() throws IOException {
		byte[] commonHeader = new byte[8];
		byte[] payloadHeader = new byte[128];
		isReader.read(commonHeader);
		isReader.read(payloadHeader);

		int dataSize = NetworkByteOrder.bytesToInt((byte) 0, payloadHeader[4], payloadHeader[5], payloadHeader[6]);
		int paddingSize = payloadHeader[7] & 0xFF;

		byte[] data = new byte[dataSize];
		byte[] padding = new byte[paddingSize];
		isReader.read(data);
		isReader.read(padding);

		return data;
	}
}
