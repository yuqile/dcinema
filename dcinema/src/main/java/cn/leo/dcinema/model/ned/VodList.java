package cn.leo.dcinema.model.ned;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class VodList {
    public static class VideoInfo {
        @JsonProperty("actors")
        public String actor;
        public String directors;
        public boolean ended;
        public String englishName;
        public String episodeNum;
        public int isHDTV;
        public int id;
        public String name;
        public String poster1;
        public String poster2;
        public String poster3;
        public String poster4;
        public String poster5;
        public String postorgin;
        public String releaseYear;
        public String score;
        public String type;
        public String gotoUrl;
        public String existLowerLevel;

        public String toString() {
            return new StringBuffer().append("name=").append(name)
                    .append(", id=").append(id).append(", rating=")
                    .append(score).append(", actor=").append(actor)
                    .append(", bigImage=").append(poster1)
                    .append(", smallImage=").append(poster2)
                    .append(", directors=").append(directors).toString();
        }
    }

    public String albumId;
    public int total;

    public ArrayList<VideoInfo> albums;

    public String toString() {
        StringBuffer stb = new StringBuffer();
        stb.append("VodList [totalCount=").append(total)
                .append(", items{");
        stb.append('\n');
        for (VideoInfo itme : albums) {
            stb.append(itme.toString());
            stb.append('\n');
        }
        stb.append("]");

        return stb.toString();
    }
}
