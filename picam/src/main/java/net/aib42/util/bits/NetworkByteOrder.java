package net.aib42.util.bits;

public class NetworkByteOrder {
	public static int bytesToInt(byte b3, byte b2, byte b1, byte b0) {
		return
			(((int) (b3 & 0xFF)) << 24) |
			(((int) (b2 & 0xFF)) << 16) |
			(((int) (b1 & 0xFF)) <<  8) |
			(((int) (b0 & 0xFF)) <<  0);
	}

	public static long bytesToLong(byte b7, byte b6, byte b5, byte b4, byte b3, byte b2, byte b1, byte b0) {
		return
			(((long) (b7 & 0xFF)) << 56) |
			(((long) (b6 & 0xFF)) << 48) |
			(((long) (b5 & 0xFF)) << 40) |
			(((long) (b4 & 0xFF)) << 32) |
			(((long) (b3 & 0xFF)) << 24) |
			(((long) (b2 & 0xFF)) << 16) |
			(((long) (b1 & 0xFF)) <<  8) |
			(((long) (b0 & 0xFF)) <<  0);
	}
}
