package cn.leo.dcinema.model;

import java.io.Serializable;

public class VideoInfo implements Serializable {
	private static final long serialVersionUID = 991029316222408987L;
	public String banben;
	public int id;
	public String img;
	public String logo;
	public String mark;
	public String qxd;
	public String title;
	public String zid;

	public String toString() {
		return (new StringBuilder("VideoInfo [id=")).append(id)
				.append(", mark=").append(mark).append(", title=")
				.append(title).append(", img=").append(img).append(", qxd=")
				.append(qxd).append(", banben=").append(banben)
				.append(", zid=").append(zid).append(", logo=").append(logo)
				.append("]").toString();
	}

}
