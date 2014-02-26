package cn.leo.dcinema.adapters;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.leo.dcinema.R;
import cn.leo.dcinema.model.VideoSet;
import java.util.List;

public class PlayerChooseArtAdapter extends BaseAdapter {

	public PlayerChooseArtAdapter(Context context1, List<VideoSet> list, int i) {
		context = context1;
		childSets = list;
		groupIndex = i;
	}

	public int getCount() {
		return childSets.size();
	}

	public Object getItem(int i) {
		return childSets.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(int i, View view, ViewGroup viewgroup) {
		if (view == null)
			view = LayoutInflater.from(context).inflate(
					R.layout.player_choose_art_item, null);
		((TextView) view.findViewById(R.id.player_choose_text))
				.setText(((VideoSet) childSets.get(i)).setName);
		return view;
	}

	public void setDataChanged(List<VideoSet> list) {
		childSets = list;
		notifyDataSetChanged();
	}

	private List<VideoSet> childSets;
	private Context context;
	@SuppressWarnings("unused")
	private int groupIndex;
}
