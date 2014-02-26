package cn.leo.dcinema.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import cn.leo.dcinema.R;

public class DetailsKeyGridAdapter extends BaseAdapter {
	public DetailsKeyGridAdapter(Context context1, List<String> list1) {
		context = context1;
		if (list1 != null)
			list = list1;
		else
			Collections.emptyList();
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int i) {
		return list.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(int i, View view, ViewGroup viewgroup) {
		TextView textview = new TextView(context);
		textview.setTextColor(-3355444);
		textview.setTextSize(20F);
		textview.setSingleLine(true);
		textview.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
		textview.setMarqueeRepeatLimit(3);
		textview.setText((CharSequence) list.get(i));
		textview.setLayoutParams(new AbsListView.LayoutParams(130, 55));
		textview.setGravity(17);
		textview.setBackgroundResource(R.drawable.video_details_btn10_selector);
		return textview;
	}

	private Context context;
	private List<String> list;
}
