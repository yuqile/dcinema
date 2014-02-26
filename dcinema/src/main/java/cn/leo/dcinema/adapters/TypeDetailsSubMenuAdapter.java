package cn.leo.dcinema.adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import cn.leo.dcinema.R;

public class TypeDetailsSubMenuAdapter extends BaseAdapter {

	public TypeDetailsSubMenuAdapter(Context context1, ArrayList<String> arraylist) {
		selcted = -1;
		context = context1;
		if (arraylist != null)
			list = arraylist;
		else
			list = new ArrayList<String>();
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
		View view1 = LayoutInflater.from(context).inflate(
				R.layout.type_details_filter_item, null);
		TextView textview = (TextView) view1.findViewById(R.id.filter_name);
		ImageView imageview = (ImageView) view1.findViewById(R.id.filter_gou);
		if (selcted == i) {
			imageview.setVisibility(0);
			view1.setBackgroundResource(R.drawable.filter_sleted);
		}
		textview.setText((CharSequence) list.get(i));
		return view1;
	}

	public void setSelctItem(int i) {
		selcted = i;
		notifyDataSetChanged();
	}

	private Context context;
	private ArrayList<String> list;
	private int selcted;
}
