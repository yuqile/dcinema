package cn.leo.dcinema.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CMDExecute {
	public String run(String[] paramArrayOfString, String paramString)
			throws IOException {
		try {
			Object localObject1 = "";
			try {
				ProcessBuilder localProcessBuilder = new ProcessBuilder(
						paramArrayOfString);
				InputStream localInputStream = null;
				byte[] arrayOfByte = null;
				if (paramString != null) {
					localProcessBuilder.directory(new File(paramString));
					localProcessBuilder.redirectErrorStream(true);
					localInputStream = localProcessBuilder.start()
							.getInputStream();
					arrayOfByte = new byte[1024];
				}
				while (true) {
					if (localInputStream.read(arrayOfByte) == -1) {
						if (localInputStream != null)
							localInputStream.close();
						return (String) localObject1;
					}
					String str = localObject1 + new String(arrayOfByte);
					localObject1 = str;
				}
			} catch (Exception localException) {
				while (true)
					localException.printStackTrace();
			}
		} finally {
		}
	}
}
