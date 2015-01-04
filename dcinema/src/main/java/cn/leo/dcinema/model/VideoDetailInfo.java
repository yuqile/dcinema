package cn.leo.dcinema.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;


public class VideoDetailInfo implements Serializable {
	private static final long serialVersionUID = 6340622314169844477L;
	public String actor;
	public String area;
	public String banben;
	public String cate;
	public String director;
	public int doubanid;
	public String dur;
	public int id;
	public String img;
	public String info;
	public String language;
	public String mark;
	public String nexturl;
	public int playcount;
	public ArrayList<VideoSource> playlist;

	@JsonProperty("new_top")
	public ArrayList<VideoInfo> recommends;
	public int setnumber;
	public String title;
	public String type;
	public String update;
	public String year;

	public String toString() {
		return "VideoDetailInfo [id=" + this.id + ", title=" + this.title
				+ ", img=" + this.img + ", type=" + this.type + ", year="
				+ this.year + ", area=" + this.area + ", dur=" + this.dur
				+ ", language=" + this.language + ", cate=" + this.cate
				+ ", director=" + this.director + ", actor=" + this.actor
				+ ", info=" + this.info + ", doubanid=" + this.doubanid
				+ ", mark=" + this.mark + ", banben=" + this.banben
				+ ", playcount=" + this.playcount + ", update=" + this.update
				+ ", setnumber=" + this.setnumber + ", playlist="
				+ this.playlist + ", recommends=" + this.recommends
				+ ", nexturl=" + this.nexturl + "]";
	}
}
