package cn.leo.dcinema.model;

import java.io.Serializable;

public class VideoTypeInfo implements Serializable {

	public VideoTypeInfo() {
	}

	public String toString() {
		return (new StringBuilder("VideoTypeInfo [tid=")).append(tid)
				.append(", name=").append(name).append(", logo=").append(logo)
				.append("]").toString();
	}

	private static final long serialVersionUID = -7043500525639160582L;
	public String logo;
	public String name;
	public String tid;
}