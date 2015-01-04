package cn.leo.dcinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class VideoSet implements Serializable {
	private static final long serialVersionUID = -2138928193462662024L;

	@JsonProperty("url")
	public String link;

	@JsonIgnore
	public ArrayList<VideoPlayUrl> playUrls;

	@JsonProperty("name")
	public String setName;

	public String toString() {
		return "VideoSet [setName=" + this.setName + ", link=" + this.link
				+ ", playUrls=" + this.playUrls + "]";
	}
}
