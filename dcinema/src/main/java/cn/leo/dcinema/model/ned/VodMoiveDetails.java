package cn.leo.dcinema.model.ned;

import java.util.ArrayList;

public class VodMoiveDetails {
    public String actors;
    public String area;
    public int areaId;
    public String category;
    public String directors;
    public boolean ended;
    public String englishName;
    public int episodeNum;
    public int id;
    public int isHDTV;
    public String name;
    public String period;
    public String plot;
    public String poster1;
    public String poster2;
    public String poster3;
    public String poster4;
    public String poster5;
    public String poster6;
    public String poster7;
    public String poster8;
    public String postorgin;
    public String releaseYear;
    public String score;
    public String type;

    public static class Video {
        public String actors;
        public String area;
        public int areaId;
        public String directors;
        public String fhdtv;
        public String hdtv;
        public int id;
        public int ldtv;
        public String name;
        public String period;
        public String plot;
        public String poster1;
        public String poster2;
        public String poster3;
        public String poster4;
        public String releaseYear;
        public String score;
        public int sdtv;
    }
    public ArrayList<Video> videos;
}
