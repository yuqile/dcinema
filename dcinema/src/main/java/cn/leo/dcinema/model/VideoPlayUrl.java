package cn.leo.dcinema.model;

import java.io.Serializable;

public class VideoPlayUrl implements Serializable {
	private static final long serialVersionUID = -3842644477264449726L;
	public String playurl;
	public SharpnessEnum sharp;

	public boolean equals(Object paramObject) {
		if ((paramObject instanceof VideoPlayUrl)) {
			VideoPlayUrl localVideoPlayUrl = (VideoPlayUrl) paramObject;
			if ((this.sharp.getIndex() != localVideoPlayUrl.sharp.getIndex())
					|| (!this.playurl
							.equalsIgnoreCase(localVideoPlayUrl.playurl)))
				;
		}
		for (boolean bool = true;;)
			return bool;
	}

	public String toString() {
		return "VideoPlayUrl [sharp=" + this.sharp + ", playurl="
				+ this.playurl + "]";
	}
}