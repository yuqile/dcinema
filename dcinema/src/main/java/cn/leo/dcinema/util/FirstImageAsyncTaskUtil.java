package cn.leo.dcinema.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import cn.leo.dcinema.effect.ImageReflect;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FirstImageAsyncTaskUtil extends AsyncTask<String, String, Bitmap> {
	private static final String TAG = "FirstImageAsyncTaskUtil";

	public FirstImageAsyncTaskUtil(Context context1, String s) {
		refimgHeight = 0;
		context = context1;
		path = s;
	}

	protected Bitmap doInBackground(String... as) {
		Log.d(TAG, "-----------doInBackground------------");
		return BitmapUtil.getBitmap(context, path, true);
	}

	public Bitmap getNetImage() {
		Bitmap bitmap = null;
		URL url;
		try {
			bitmap = null;
			url = new URL(path);
			HttpURLConnection httpurlconnection = (HttpURLConnection) url
					.openConnection();
			httpurlconnection.setDoInput(true);
			httpurlconnection.connect();
			InputStream inputstream = httpurlconnection.getInputStream();
			bitmap = BitmapFactory.decodeStream(inputstream);
			inputstream.close();
			System.gc();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		return bitmap;
	}

	protected void onPostExecute(Bitmap bitmap) {
		super.onPostExecute(bitmap);
		if (bitmap != null && imageView != null) {
			imageView.setImageBitmap(bitmap);
			if (refimg != null) {
				if (view != null)
					bitmap = ImageReflect.convertViewToBitmap(view);
				refimg.setImageBitmap(ImageReflect.createReflectedImage(bitmap,
						refimgHeight));
			}
		}
	}

	protected void onPreExecute() {
		super.onPreExecute();
		Log.d(TAG, "-----------onPreExecute------------");
	}

	public void setParams(ImageView imageview, ImageView imageview1, int i,
			View view1) {
		imageView = imageview;
		refimg = imageview1;
		refimgHeight = i;
		view = view1;
	}

	private Context context;
	private ImageView imageView;
	private String path;
	private ImageView refimg;
	private int refimgHeight;
	private View view;
}
