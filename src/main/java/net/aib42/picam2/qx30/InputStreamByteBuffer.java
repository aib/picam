package net.aib42.picam2.qx30;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class InputStreamByteBuffer {
	private static final int BUFFER_SIZE = 1024;

	private byte[] bytes;
	private ByteBuffer byteBuffer;

	public InputStreamByteBuffer(InputStream inputStream) {
		bytes = new byte[BUFFER_SIZE];
		try {
			inputStream.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byteBuffer = ByteBuffer.wrap(bytes);
		byteBuffer.order(ByteOrder.BIG_ENDIAN);
	}

	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}
}
