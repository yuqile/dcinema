package cn.leo.dcinema.model.ned;

import java.util.ArrayList;

public class VodMoiveAddr {

	public static class Gslb {
		public int timeout;
		public String subgslb;
		public String nodetimeout;
	}

	public ArrayList<Gslb> gslb;
	public String newUrl;
	public long duration;
	public String sessionId;
	public String playUrl;
	public long fileSize;
	public long vid;
	public String md5;
	public long mmsid;
	public String iptvVideoFileId;
}
