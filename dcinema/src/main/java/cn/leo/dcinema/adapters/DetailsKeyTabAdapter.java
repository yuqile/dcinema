package cn.leo.dcinema.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import java.util.Collections;
import java.util.List;
import cn.leo.dcinema.R;

public class DetailsKeyTabAdapter extends BaseAdapter {

	public DetailsKeyTabAdapter(Context context, List<String> list1) {
		this.context = context;
		context.obtainStyledAttributes(R.styleable.MyGallery).recycle();
		if (list1 == null)
			Collections.emptyList();
		else
			list = list1;
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
		Button button = new Button(context);
		button.setTextColor(-3355444);
		button.setTextSize(20F);
		button.setSingleLine(true);
		button.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
		button.setMarqueeRepeatLimit(3);
		button.setText((CharSequence) list.get(i));
		button.setLayoutParams(new Gallery.LayoutParams(120, 55));
		button.setGravity(17);
		button.setBackgroundResource(R.drawable.video_details_btn10_selector);
		return button;
	}

	public void setSelectedTab(int i) {
		if (i != selectedTab) {
			selectedTab = i;
			notifyDataSetChanged();
		}
	}

	private Context context;
	private List<String> list;
	private int selectedTab;
}