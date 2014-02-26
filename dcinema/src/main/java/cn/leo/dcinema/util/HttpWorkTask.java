package cn.leo.dcinema.util;

import android.os.AsyncTask;

public class HttpWorkTask<T> extends AsyncTask<Void, Void, T> {
	private ParseCallBack<T> parseCallBack;
	private PostCallBack<T> postCallBack;

	public HttpWorkTask(ParseCallBack<T> paramParseCallBack,
			PostCallBack<T> paramPostCallBack) {
		this.parseCallBack = paramParseCallBack;
		this.postCallBack = paramPostCallBack;
	}

	@Override
	protected T doInBackground(Void... params) {
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