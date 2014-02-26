package cn.leo.dcinema.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	public static interface PostCallBack {

		public abstract void post(Bitmap bitmap);
	}

	public BitmapWorkerTask(Context context, ImageView imageview, boolean flag,
			boolean flag1) {
		this.bitmapUtil = BitmapUtil.getInstance(context);
		this.imageView = imageview;
		this.isLruCache = flag;
		this.isDiskLruCache = flag1;
	}

	protected Bitmap doInBackground(String... as) {
		Bitmap bitmap = null;
		String s = as[0];
		if (!(TextUtils.isEmpty(s))) {
			Log.d(TAG,
					(new StringBuilder("doInBackground bitmap url ="))
							.append(s).append("  ")
							.append(Thread.currentThread().getName())
							.toString());
			if (true && isLruCache) {
				Log.d(TAG,"get bitmap LruCache");
				bitmap = bitmapUtil.getBitmapFromMemory(s);
			}
			if (bitmap == null && isDiskLruCache) {
				bitmap = bitmapUtil.getBitmapFromDisk(s);
				Log.d(TAG,"get bitmap DiskLruCache");
			}
			if (bitmap == null) {
				bitmap = bitmapUtil.getBitmapFromNet(s, width, height);
				Log.d(TAG,"get bitmap NET");
			}
			bitmapUtil.addToCache(s, bitmap, isLruCache, isDiskLruCache);
		}
		return bitmap;
	}

	protected void onCancelled(Bitmap bitmap) {
		Log.d(TAG, "onCancelled");
		if (bitmap != null && bitmap.isRecycled())
			bitmap.recycle();
		super.onCancelled(bitmap);
	}

	protected void onPostExecute(Bitmap bitmap) {
		Log.d(TAG, (new StringBuilder("onPostExecute bitmap =")).append(bitmap)
				.append(" , ").append(Thread.currentThread().getName())
				.toString());
		if (imageView == null || bitmap == null) {
			if (imageView != null && bitmap == null && defaultDrawable != null)
				imageView.setImageDrawable(defaultDrawable);
		} else {
			imageView.setImageBitmap(bitmap);
		}
		if (callback != null)
			callback.post(bitmap);
		super.onPostExecute(bitmap);
	}

	public BitmapWorkerTask setCallback(PostCallBack postcallback) {
		callback = postcallback;
		return this;
	}

	public BitmapWorkerTask setDefaultDrawable(Drawable drawable) {
		defaultDrawable = drawable;
		return this;
	}

	private static final String TAG = "BitmapWorkerTask";
	private BitmapUtil bitmapUtil;
	private PostCallBack callback;
	private Drawable defaultDrawable;
	private int height;
	private ImageView imageView;
	private boolean isDiskLruCache;
	private boolean isLruCache;
	private int width;
}