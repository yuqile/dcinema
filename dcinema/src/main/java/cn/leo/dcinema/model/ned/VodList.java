package cn.leo.dcinema.model.ned;

import java.util.ArrayList;

public class VodList {
	public static class VideoInfo {
		public String name;
		public int id;
		public String rating;
		public String actor;
		public String bigImage;
		public String smallImage;
		public String directors;
		public String gotoUrl;
		public String existLowerLevel;

		public String toString() {
			return new StringBuffer().append("name=").append(name)
					.append(", id=").append(id).append(", rating=")
					.append(rating).append(", actor=").append(actor)
					.append(", bigImage=").append(bigImage)
					.append(", smallImage=").append(smallImage)
					.append(", directors=").append(directors).toString();
		}
	}

	public int totalCount;
	public ArrayList<VideoInfo> items;

	public String toString() {
		StringBuffer stb = new StringBuffer();
		stb.append("VodList [totalCount=").append(totalCount)
				.append(", items{");
		stb.append('\n');
		for (VideoInfo itme : items) {
			stb.append(itme.toString());
			stb.append('\n');
		}
		stb.append("]");

		return stb.toString();
	}
}
