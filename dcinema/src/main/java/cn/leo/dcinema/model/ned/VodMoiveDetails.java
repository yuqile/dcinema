package cn.leo.dcinema.model.ned;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonProperty;

public class VodMoiveDetails {
	public static class Related {
		public String name;
		public int iptvAlbumId;
		public int categoryId;
		public String releaseDate;
		public String bigImage;
		public String smallImage;
		public String thumbnailImage;
		public String detailUrl;
	}

	public static class D3Type {
		public String names[];
		public String series[];

	}

	public static class Streams {
		@JsonProperty("3d720p")
		public D3Type d720p;
		@JsonProperty("3d1080p6m")
		public D3Type d1080p6m;
	}

	public static class Data {
		public ArrayList<Related> relatedAlbum;
		public ArrayList<Related> actorRelated;
		public ArrayList<Related> directorRelated;
		public Streams streams;

		public String name;
		public String description;
		public String duration;
		public String subCategories;
		public int iptvAlbumId;
		public int categoryId;
		public String area;
		public String rating;
		public String releaseDate;
		public String bigImage;
		public String smallImage;
		public String thumbnailImage;
		public String directors;
		public String actors;
		public String lookpoint;
	}

	public Data data;
}
