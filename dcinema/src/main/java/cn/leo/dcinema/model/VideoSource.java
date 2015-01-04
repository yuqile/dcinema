package cn.leo.dcinema.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;


public class VideoSource implements Serializable {
	private static final long serialVersionUID = 4134329126322121094L;

	@JsonProperty("list")
	public ArrayList<VideoSet> sets;

	@JsonProperty("site")
	public String sourceName;

	public String toString() {
		return "VideoSource [sourceName=" + this.sourceName + ", sets="
				+ this.sets + "]";
	}
}