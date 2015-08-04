package net.aib42.picam2.qx30;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class Requester {
	public static String LIVEVIEW_STREAM_URL = "http://192.168.8.200:5000/liveview/liveviewstream";

	private HttpClient client;
	private HttpGet request;
	private InputStream responseInputStream;
	
	public Requester() {
		client = HttpClients.createDefault();
		request = new HttpGet(LIVEVIEW_STREAM_URL);
	}

	public void makeRequest() throws IllegalStateException, IOException {
		HttpResponse response = client.execute(request);
		responseInputStream = response.getEntity().getContent();
	}

	public InputStream getNextJpegInputStream() throws IOException {
		byte[] commonHeader = new byte[8];
		byte[] payloadHeader = new byte[128];
		responseInputStream.read(commonHeader);
		responseInputStream.read(payloadHeader);

String s = "Common header:\n";
for (int i=0; i<commonHeader.length; ++i) {
	s += String.format("%02x ", commonHeader[i]);
}
System.out.println(s);
		
		int dataSize =
			(((int) (payloadHeader[4] & 0xFF)) << 16) +
			(((int) (payloadHeader[5] & 0xFF)) << 8) +
			(((int) (payloadHeader[6] & 0xFF)) << 0);
		int paddingSize = payloadHeader[7] & 0xFF;

System.out.println("Data size is " + dataSize);
System.out.println("Padding is " + paddingSize);

		byte[] data = new byte[dataSize];
		byte[] padding = new byte[paddingSize];
		responseInputStream.read(data);
		responseInputStream.read(padding);

String ds = "Data:\n";
for (int i=0; i<data.length; ++i) {
	ds += String.format("%02x ", data[i]);
}
System.out.println(ds);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		return bis;
	}
}
