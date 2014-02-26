package cn.leo.dcinema.adapters;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.leo.dcinema.R;
import cn.leo.dcinema.model.VideoSet;
import java.util.ArrayList;
import java.util.List;

public class DetailsKeyListAdapter extends BaseAdapter {

	public DetailsKeyListAdapter(Context context1, List<VideoSet> list) {
		context = context1;
		inflater = LayoutInflater.from(context1);
		if (list != null)
			sets = list;
		else
			sets = new ArrayList<VideoSet>();
	}

	public int getCount() {
		return sets.size();
	}

	public Object getItem(int i) {
		return sets.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(int i, View view, ViewGroup viewgroup) {
		View view1 = inflater.inflate(R.layout.video_details_choose_arts_item,
				null);
		((TextView) view1.findViewById(R.id.text)).setText(((VideoSet) sets
				.get(i)).setName);
		return view1;
	}

	public void setDataChanged(List<VideoSet> list) {
		sets = list;
		notifyDataSetChanged();
	}

	@SuppressWarnings("unused")
	private Context context;
	private LayoutInflater inflater;
	private List<VideoSet> sets;
}
