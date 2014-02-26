package cn.leo.dcinema.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;
import cn.leo.dcinema.R;
import cn.leo.dcinema.model.VideoInfo;
import cn.leo.dcinema.util.FirstImageAsyncTaskUtil;
import java.util.ArrayList;

public class HotVideoAdapter extends BaseAdapter {
	class ViewHolder {
		private ImageView poster;
		private ImageView refimg;
		private TextView videoName;
	}

	public HotVideoAdapter(Context context1, ArrayList<VideoInfo> arraylist) {
		context = context1;
		if (arraylist != null)
			beans = arraylist;
		else
			beans = new ArrayList<VideoInfo>();
	}

	public int getCount() {
		return beans.size();
	}

	public Object getItem(int i) {
		return beans.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(int i, View view, ViewGroup viewgroup) {
		ViewHolder viewholder;
		VideoInfo videoinfo;
		FirstImageAsyncTaskUtil firstimageasynctaskutil;
		if (view == null) {
			viewholder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.video_details_recommend_item, null);
			viewholder.poster = (ImageView) view
					.findViewById(R.id.details_recommend_poster);
			viewholder.videoName = (TextView) view
					.findViewById(R.id.details_recommend_name);
			viewholder.refimg = (ImageView) view
					.findViewById(R.id.details_recommend_ref);
			view.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) view.getTag();
		}
		videoinfo = beans.get(i);
		viewholder.videoName.setText(videoinfo.title);
		firstimageasynctaskutil = new FirstImageAsyncTaskUtil(context,
				beans.get(i).img);
		firstimageasynctaskutil.setParams(viewholder.poster, viewholder.refimg,
				50, null);
//		firstimageasynctaskutil.execute((Object)new String[0]);
		return view;
	}

	private ArrayList<VideoInfo> beans;
	private Context context;
}
