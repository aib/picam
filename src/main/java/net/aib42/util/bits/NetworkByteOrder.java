package net.aib42.util.bits;

public class NetworkByteOrder {
	public static int bytesToInt(byte b3, byte b2, byte b1, byte b0) {
		return
			(((int) (b3 & 0xFF)) << 24) |
			(((int) (b2 & 0xFF)) << 16) |
			(((int) (b1 & 0xFF)) <<  8) |
			(((int) (b0 & 0xFF)) <<  0);
	}
}
