package cn.leo.dcinema.model;

import java.util.ArrayList;

public class VideoList {
	public int maxpage;
	public int punpage;
	public ArrayList<VideoInfo> video;
	public int video_count;
	public int zurpage;

	public String toString() {
		return "VideoList [zurpage=" + this.zurpage + ", punpage="
				+ this.punpage + ", maxpage=" + this.maxpage + ", video_count="
				+ this.video_count + ", videos=" + this.video + "]";
	}
}