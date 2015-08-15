package net.aib42.util.stream;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamBlockingReader {
	private InputStream source;

	public InputStreamBlockingReader(InputStream source) {
		this.source = source;
	}

	public void read(byte[] b) throws IOException {
		int off = 0;
		int len = b.length;

		while (len > 0) {
			int read = source.read(b, off, len);
			len -= read;
			off += read;
		}
	}
}
