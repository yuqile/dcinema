package cn.leo.dcinema.util;

import android.os.AsyncTask;

public class HDfanParseTask<T> extends AsyncTask<String, Integer, T> {
	private ParseCallBack<T> parseCallBack;
	private PostCallBack<T> postCallBack;

	public HDfanParseTask(ParseCallBack<T> parseCallBack,
			PostCallBack<T> postCallBack) {
		super();
		this.parseCallBack = parseCallBack;
		this.postCallBack = postCallBack;
	}

	@Override
	protected T doInBackground(String... params) {
		T obj;
		if (parseCallBack != null)
			obj = parseCallBack.onParse();
		else
			obj = null;
		return obj;
	}

	@Override
	protected void onPostExecute(T result) {
		// TODO Auto-generated method stub
		if (this.postCallBack != null)
			this.postCallBack.onPost(result);
	}

	public static abstract interface ParseCallBack<T> {
		public abstract T onParse();
	}

	public static abstract interface PostCallBack<T> {
		public abstract void onPost(T paramT);
	}

}
