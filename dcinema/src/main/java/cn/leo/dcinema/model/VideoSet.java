package cn.leo.dcinema.model;

import java.io.Serializable;
import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

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
