package cn.leo.dcinema.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FileUtils {
	private static final String TAG = "FileUitl------读取文件工具类";
	private static final String sdcardPath = Environment
			.getExternalStorageDirectory().getPath();
	private static String padFilePath = new StringBuffer().append(sdcardPath)
			.append("/driver.ini").toString();
	private static String path = new StringBuffer().append(sdcardPath)
			.append("/GMYZ/log/").toString();

	static class Fileter implements FilenameFilter {
		private final String ext;

		public Fileter(String s) {
			ext = s;
		}

		@Override
		public boolean accept(File file, String s) {
			// TODO Auto-generated method stub
			return s.endsWith(ext);
		}

	}

	public FileUtils() {
	}

	public static boolean checkFileExists(String s) {
		boolean flag;
		if (!s.equals(""))
			flag = (new File((new StringBuilder(String.valueOf(Environment
					.getExternalStorageDirectory().toString()))).append(s)
					.toString())).exists();
		else
			flag = false;
		return flag;
	}

	public static boolean checkSaveLocationExists() {
		boolean flag;
		if (Environment.getExternalStorageState().equals("mounted"))
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static void copyFile(String s, String s1) {
		try {
			if ((new File(s)).exists()) {
				FileInputStream fileinputstream = new FileInputStream(s);
				File file = new File(s1);
				if (!file.exists())
					file.createNewFile();
				FileOutputStream fileoutputstream = new FileOutputStream(s1);
				byte abyte0[] = new byte[1444];
				do {
					int j = fileinputstream.read(abyte0);
					if (j == -1) {
						fileoutputstream.flush();
						fileoutputstream.close();
						fileinputstream.close();
						break;
					}
					fileoutputstream.write(abyte0, 0, j);
				} while (true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static boolean createDirectory(String s) {
		boolean flag;
		if (!s.equals("")) {
			(new File((new StringBuilder(String.valueOf(Environment
					.getExternalStorageDirectory().toString()))).append(s)
					.toString())).mkdir();
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	public static File createFile(String s, String s1) {
		File file = new File(s);
		if (!file.exists())
			file.mkdirs();
		return new File(s, (new StringBuilder(String.valueOf(s1))).append(s1)
				.toString());
	}

	public static boolean delFile(String s) {
		boolean flag = false;
		File file = new File(s);
		try {
			if (file.exists()) {
				file.delete();
				flag = true;
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return flag;
	}

	public static boolean deleteDirectory(String s) {
		SecurityManager securitymanager = new SecurityManager();
		boolean flag = false;
		if (s.equals("")) {
			flag = false;
		} else {
			File file;
			file = new File((new StringBuilder(String.valueOf(Environment
					.getExternalStorageDirectory().toString()))).append(s)
					.toString());
			securitymanager.checkDelete(file.toString());
			if (!file.isDirectory()) {
				flag = false;
			} else {
				String as[];
				as = file.list();
				for (int i = 0; i < as.length; i++) {
					(new File((new StringBuilder(
							String.valueOf(file.toString()))).append("/")
							.append(as[i].toString()).toString())).delete();
				}
			}
		}
		return flag;
	}

	public static boolean deleteFile(String s) {
		SecurityManager securitymanager = new SecurityManager();
		boolean flag = false;
		if (!s.equals("")) {
			File file;
			file = new File((new StringBuilder(String.valueOf(Environment
					.getExternalStorageDirectory().toString()))).append(s)
					.toString());
			securitymanager.checkDelete(file.toString());
			if (file.isFile()) {
				try {
					file.delete();
				} catch (SecurityException securityexception) {
					securityexception.printStackTrace();
					flag = false;
				}
				flag = true;
			}
		}
		return flag;
	}

	public static String formatFileSize(long l) {
		DecimalFormat decimalformat = new DecimalFormat("#.00");
		String s;
		if (l < 1024L)
			s = (new StringBuilder(String.valueOf(decimalformat.format(l))))
					.append("B").toString();
		else if (l < 1048576L)
			s = (new StringBuilder(String.valueOf(decimalformat
					.format((double) l / 1024D)))).append("KB").toString();
		else if (l < 1073741824L)
			s = (new StringBuilder(String.valueOf(decimalformat
					.format((double) l / 1048576D)))).append("MB").toString();
		else
			s = (new StringBuilder(String.valueOf(decimalformat
					.format((double) l / 1073741824D)))).append("G").toString();
		return s;
	}

	public static long getDirSize(File file) {
		long l;
		l = 0L;
		if (file != null && file.isDirectory()) {
			l = 0L;
			File afile[] = file.listFiles();
			int i = afile.length;
			int j = 0;
			while (j < i) {
				File file1 = afile[j];
				if (file1.isFile())
					l += file1.length();
				else if (file1.isDirectory())
					l = l + file1.length() + getDirSize(file1);
				j++;
			}
		}
		return l;
	}

	public static String getFileFormat(String s) {
		String s1;
		if (s == null || "".equals(s))
			s1 = "";
		else
			s1 = s.substring(1 + s.lastIndexOf('.'));
		return s1;
	}

	public static String getFileName(String s) {
		String s1;
		if (s == null || "".equals(s))
			s1 = "";
		else
			s1 = s.substring(1 + s.lastIndexOf(File.separator));
		return s1;
	}

	public static String getFileNameNoFormat(String s) {
		String s1;
		if (s == null || "".equals(s)) {
			s1 = "";
		} else {
			int i = s.lastIndexOf('.');
			s1 = s.substring(1 + s.lastIndexOf(File.separator), i);
		}
		return s1;
	}

	public static long getFileSize(String s) {
		long l = 0L;
		File file = new File(s);
		if (file != null && file.exists())
			l = file.length();
		return l;
	}

	public static String getFileSize(long l) {
		String s;
		if (l <= 0L) {
			s = "0";
		} else {
			DecimalFormat decimalformat = new DecimalFormat("##.##");
			float f = (float) l / 1024F;
			if (f >= 1024F)
				s = (new StringBuilder(String.valueOf(decimalformat
						.format(f / 1024F)))).append("M").toString();
			else
				s = (new StringBuilder(String.valueOf(decimalformat.format(f))))
						.append("K").toString();
		}
		return s;
	}

	public static long getFreeDiskSpace() {
		String s = Environment.getExternalStorageState();
		long l = 0L;
		long l1;
		if (s.equals("mounted")) {
			try {
				StatFs statfs = new StatFs(Environment
						.getExternalStorageDirectory().getPath());
				l = ((long) statfs.getBlockSize() * (long) statfs
						.getAvailableBlocks()) / 1024L;
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			l1 = l;
		} else {
			l1 = -1L;
		}
		return l1;
	}

	public static List<File> list(File file, String s, String s1, String s2,
			List<File> list1) {
		listFile(file, s, s2, s1, list1);
		File afile[] = file.listFiles();
		int i = 0;
		do {
			if (i >= afile.length)
				return null;
			File file1 = afile[i];
			if (file1.isDirectory())
				list(file1, s, s1, s2, list1);
			i++;
		} while (true);
	}

	private static List<File> listFile(File file, String s, String s1,
			String s2, List<File> list1) {
		File afile[];
		afile = file.listFiles(new Fileter(s2));
		File file1;
		for (int i = 0; i < afile.length; i++) {
			file1 = afile[i];
			if (file1.getName().toLowerCase(Locale.getDefault())
					.indexOf(s.toLowerCase(Locale.getDefault())) >= 0) {
				if (!s1.equals("1"))
					break;
				list1.add(file1);
			}
			if (file1.isDirectory() && s1.equals("2"))
				list1.add(file1);
			else if (!file1.isDirectory() && s1.equals("3"))
				list1.add(file1);
		}
		return list1;
	}

	public static void modifyFile(File file) {
		String s = (new StringBuilder("chmod -R 777 ")).append(
				file.getAbsolutePath()).toString();
		try {
			Runtime.getRuntime().exec(s).waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}

	public static void moveFile(String s, String s1) {
		copyFile(s, s1);
		delFile(s);
	}

	public static String read(Context context, String s) {
		String s2 = null;
		try {
			s2 = readInStream(context.openFileInput(s));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
		return s2;
	}

	private static String readInStream(FileInputStream fileinputstream) {
		String s;
		try {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			byte abyte0[] = new byte[512];
			do {
				int i = fileinputstream.read(abyte0);
				if (i == -1) {
					bytearrayoutputstream.close();
					fileinputstream.close();
					s = bytearrayoutputstream.toString();
					break;
				}
				bytearrayoutputstream.write(abyte0, 0, i);
			} while (true);
		} catch (IOException ioexception) {
			s = null;
		}
		return s;
	}

	public static String saveLog(String s, String s1, boolean flag, String s2) {
		String s3 = null;
		if (flag) {
			StringBuffer stringbuffer;
			String s6;
			String as1[];
			SimpleDateFormat simpledateformat = new SimpleDateFormat(
					"yyyyMMddHHmmss", Locale.getDefault());
			Date date = new Date(System.currentTimeMillis());
			stringbuffer = new StringBuffer();
			String as[];
			SimpleDateFormat simpledateformat1;
			String s4;
			String s5;
			File file;
			File file1;
			FileOutputStream fileoutputstream;
			OutputStreamWriter outputstreamwriter;
			BufferedWriter bufferedwriter;
			if (s != null && !s.equals(""))
				stringbuffer.append((new StringBuilder(String
						.valueOf(simpledateformat.format(date)))).append("【")
						.append(s).append("】\n").toString());
			else
				stringbuffer.append((new StringBuilder(String
						.valueOf(simpledateformat.format(date))))
						.append("\t\t").toString());
			as = s1.split("body");
			if (as.length <= 1)
				return null;
			s6 = (new StringBuilder(String.valueOf(""))).append(as[0])
					.append("\n").append("body").toString();
			as1 = as[1].replaceAll(",", "").split("]");
			for (int i = 0; i < as1.length; i++) {
				stringbuffer.append(s6.substring(0, -4 + s6.length()));
				stringbuffer.append("\n");
				simpledateformat1 = new SimpleDateFormat("yyyyMMdd",
						Locale.getDefault());
				(new PrintWriter(new StringWriter())).close();
				s4 = simpledateformat1.format(new Date(System
						.currentTimeMillis()));
				if (s2 == null || s2.equals(""))
					s2 = "log";
				s5 = (new StringBuilder(String.valueOf(s2))).append("-")
						.append(s4).append(".txt").toString();
				if (Environment.getExternalStorageState().equals("mounted")) {
					file = new File(path);
					if (!file.exists())
						file.mkdirs();
					file1 = new File((new StringBuilder(String.valueOf(path)))
							.append(s5).toString());
					if (!file1.exists())
						try {
							file1.createNewFile();
							fileoutputstream = new FileOutputStream(file1, true);
							outputstreamwriter = new OutputStreamWriter(
									fileoutputstream, "GBK");
							bufferedwriter = new BufferedWriter(
									outputstreamwriter);
							bufferedwriter.write(stringbuffer.toString());
							bufferedwriter.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Log.e("FileUitl------读取文件工具类",
									"an error occured while writing file...", e);
						}

				}
				s3 = s5;
				if (i != -2 + as1.length)
					s6 = (new StringBuilder(String.valueOf(s6))).append(as1[i])
							.append("],\n").toString();
				else
					s6 = (new StringBuilder(String.valueOf(s6))).append(as1[i])
							.toString();
			}
			stringbuffer.append(s1);
		} else {
			return null;
		}
		return s3;
	}

	public static String saveLog(String s, boolean flag) {
		return saveLog(s, "", flag, null);
	}

	public static String saveLog(String s, boolean flag, String s1) {
		return saveLog(s, "", flag, s1);
	}

	public static byte[] toBytes(InputStream inputstream) throws IOException {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		do {
			int i = inputstream.read();
			if (i == -1) {
				byte abyte0[] = bytearrayoutputstream.toByteArray();
				bytearrayoutputstream.close();
				return abyte0;
			}
			bytearrayoutputstream.write(i);
		} while (true);
	}

	public static void write(Context context, String s, String s1) {
		if (s1 == null)
			s1 = "";
		FileOutputStream fileoutputstream;
		try {
			fileoutputstream = context.openFileOutput(s, 0);
			fileoutputstream.write(s1.getBytes());
			fileoutputstream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	public static boolean writeFile(byte abyte0[], String s, String s1) {
		FileOutputStream fileoutputstream = null;
		boolean flag = false;
		String s2 = "";
		File file;
		File file1;
		if (Environment.getExternalStorageState().equals("mounted"))
			s2 = (new StringBuilder())
					.append(Environment.getExternalStorageDirectory())
					.append(File.separator).append(s).append(File.separator)
					.toString();
		else
			flag = false;
		file = new File(s2);
		if (!file.exists())
			file.mkdirs();
		file1 = new File((new StringBuilder(String.valueOf(s2))).append(s1)
				.toString());
		fileoutputstream = null;
		try {
			fileoutputstream = new FileOutputStream(file1);
			fileoutputstream.write(abyte0);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		flag = true;

		try {
			fileoutputstream.close();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
		return flag;
	}

	public long getFileList(File file) {
		File afile[] = file.listFiles();
		long l = afile.length;
		int i = afile.length;
		int j = 0;
		do {
			if (j >= i)
				return l;
			File file1 = afile[j];
			if (file1.isDirectory())
				l = (l + getFileList(file1)) - 1L;
			j++;
		} while (true);
	}

	public String readSDFile(String s) {
		StringBuffer stringbuffer;
		File file;
		String s1 = null;
		stringbuffer = new StringBuffer();
		file = new File((new StringBuilder(String.valueOf(padFilePath)))
				.append("//").append(s).toString());
		if (file.exists()) {
			s1 = "";
		} else {
			try {
				FileInputStream fileinputstream = new FileInputStream(file);
				while (true) {
					int i = fileinputstream.read();
					if (i != -1) {
						char c = (char) i;
						stringbuffer.append(c);
					} else {
						fileinputstream.close();
						break;
					}
				}
				s1 = stringbuffer.toString();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
			}
		}
		return s1;
	}
}
