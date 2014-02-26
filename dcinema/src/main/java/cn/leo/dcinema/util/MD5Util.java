package cn.leo.dcinema.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static void appendHexPair(byte byte0, StringBuffer stringbuffer) {
		char c = hexDigits[(byte0 & 240) >> 4];
		char c1 = hexDigits[byte0 & 15];
		stringbuffer.append(c);
		stringbuffer.append(c1);
	}

	private static String bufferToHex(byte abyte0[]) {
		return bufferToHex(abyte0, 0, abyte0.length);
	}

	private static String bufferToHex(byte abyte0[], int i, int j) {
		StringBuffer stringbuffer = new StringBuffer(j * 2);
		int k = i + j;
		int l = i;
		do {
			if (l >= k)
				return stringbuffer.toString();
			appendHexPair(abyte0[l], stringbuffer);
			l++;
		} while (true);
	}

	public static boolean checkPassword(String s, String s1) {
		return getMD5String(s).equals(s1);
	}

	public static String getFileMD5String(File file) throws IOException {
		FileInputStream fileinputstream = new FileInputStream(file);
		byte abyte0[] = new byte[1024];
		do {
			int i = fileinputstream.read(abyte0);
			if (i <= 0) {
				fileinputstream.close();
				return bufferToHex(messagedigest.digest());
			}
			messagedigest.update(abyte0, 0, i);
		} while (true);
	}

	public static String getFileMD5String_old(File file) throws IOException {
		FileInputStream filein = new FileInputStream(file);
		java.nio.MappedByteBuffer mappedbytebuffer = (filein).getChannel().map(
				java.nio.channels.FileChannel.MapMode.READ_ONLY, 0L,
				file.length());
		messagedigest.update(mappedbytebuffer);
		byte[] bytebuffer = messagedigest.digest();
		filein.close();
		return bufferToHex(bytebuffer);
	}

	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public static String getMD5String(byte abyte0[]) {
		messagedigest.update(abyte0);
		return bufferToHex(messagedigest.digest());
	}

	protected static char hexDigits[];
	protected static MessageDigest messagedigest = null;

	static {
		char ac[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		hexDigits = ac;
		messagedigest = null;
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			while (true) {
				System.err.println(MD5Util.class.getName()
						+ "初始化失败，MessageDigest不支持MD5Util。");
				localNoSuchAlgorithmException.printStackTrace();
			}
		}
	}
}
