package cn.leo.dcinema.adapters;

import android.content.Context;
import android.text.Html;
import android.view.*;
import android.widget.*;
import cn.leo.dcinema.R;
import cn.leo.dcinema.model.VideoInfo;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

public class VideoInfoAdapter extends BaseAdapter {
	class ViewHolder {
		private TextView banben;
		private ImageView poster;
		private ImageView spuerHd;
		private TextView videoName;
	}

	public VideoInfoAdapter(Context context1, ArrayList<VideoInfo> arraylist) {
		this(context1, arraylist, false);
	}

	public VideoInfoAdapter(Context context1, ArrayList<VideoInfo> arraylist,
			boolean flag) {
		context = context1;
		setVideos(arraylist);
		isSearch = flag;
		fb = FinalBitmap.create(context1, context1.getCacheDir().toString());
		fb.configLoadingImage(R.drawable.default_film_img);
		fb.configTransitionDuration(1000);
	}

	public void changData(ArrayList<VideoInfo> arraylist) {
		setVideos(arraylist);
		notifyDataSetChanged();
	}

	public int getCount() {
		return videos.size();
	}

	public Object getItem(int i) {
		return videos.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(int i, View view, ViewGroup viewgroup) {
		ViewHolder viewholder;
		VideoInfo videoinfo;
		if (view == null) {
			viewholder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.type_details_item, null);
			viewholder.poster = (ImageView) view
					.findViewById(R.id.video_poster);
			viewholder.spuerHd = (ImageView) view
					.findViewById(R.id.video_superHD);
			viewholder.banben = (TextView) view.findViewById(R.id.video_banben);
			viewholder.videoName = (TextView) view
					.findViewById(R.id.video_name);
			view.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) view.getTag();
		}
		videoinfo = videos.get(i);
		if (isSearch)
			viewholder.videoName.setText(Html.fromHtml(videoinfo.title));
		else
			viewholder.videoName.setText(videoinfo.title);
		// if (videoinfo.qxd != null && videoinfo.qxd.contains("高清")) {
		// viewholder.spuerHd.setImageResource(R.drawable.sd);
		// viewholder.spuerHd.setVisibility(View.VISIBLE);
		// } else if (videoinfo.qxd != null && videoinfo.qxd.contains("超清")) {
		// viewholder.spuerHd.setImageResource(R.drawable.hd);
		// viewholder.spuerHd.setVisibility(View.VISIBLE);
		// } else {
		viewholder.spuerHd.setVisibility(View.GONE);
		// }
		// viewholder.banben.setText(videoinfo.img);
		fb.display(viewholder.poster, videoinfo.img);
		return view;
	}

	public void setVideos(ArrayList<VideoInfo> arraylist) {
		if (arraylist != null)
			videos = arraylist;
		else
			videos = new ArrayList<VideoInfo>();
	}

	private Context context;
	private FinalBitmap fb;
	private boolean isSearch;
	private ArrayList<VideoInfo> videos;
}
