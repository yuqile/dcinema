package cn.leo.dcinema.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import java.util.List;
import cn.leo.dcinema.R;
import cn.leo.dcinema.model.VideoSet;

public class PlayerChooseTvAdapter extends BaseAdapter {

	public PlayerChooseTvAdapter(Context context1, List<VideoSet> list, int i) {
		context = context1;
		childSets = list;
		groupIndex = i;
	}

	private Button createSetBTN(int i) {
		Button button = new Button(context);
		button.setWidth(120);
		button.setHeight(55);
		button.setText((new StringBuilder("第")).append(i + 1).append("集")
				.toString());
		button.setTextSize(18F);
		button.setTag(Integer.valueOf(i - 1));
		button.setFocusable(false);
		button.setFocusableInTouchMode(false);
		button.setBackgroundResource(R.drawable.video_details_btn_selector);
		button.setTextColor(-3355444);
		return button;
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
		return createSetBTN(i + 30 * groupIndex);
	}

	public void setDataChanged(List<VideoSet> list, int i) {
		childSets = list;
		groupIndex = i;
		notifyDataSetChanged();
	}

	private List<VideoSet> childSets;
	private Context context;
	private int groupIndex;
}
