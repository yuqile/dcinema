package cn.leo.dcinema.effect;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.view.View;

public class ImageReflect {
	private static int reflectImageHeight = 100;

	public static Bitmap convertViewToBitmap(View paramView) {
		paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0),
				View.MeasureSpec.makeMeasureSpec(0, 0));
		paramView.layout(0, 0, paramView.getMeasuredWidth(),
				paramView.getMeasuredHeight());
		paramView.buildDrawingCache();
		return paramView.getDrawingCache();
	}

	public static Bitmap createCutReflectedImage(Bitmap bitmap, int i) {
		int j = bitmap.getWidth();
		int k = bitmap.getHeight();
		Bitmap bitmap2;
		if (k <= i + reflectImageHeight) {
			bitmap2 = null;
		} else {
			Matrix matrix = new Matrix();
			matrix.preScale(1.0F, -1F);
			System.out.println(k - reflectImageHeight - i);
			Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, k
					- reflectImageHeight - i, j, reflectImageHeight, matrix,
					true);
			bitmap2 = Bitmap.createBitmap(j, reflectImageHeight,
					android.graphics.Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap2);
			canvas.drawBitmap(bitmap1, 0.0F, 0.0F, null);
			LinearGradient lineargradient = new LinearGradient(0.0F, 0.0F,
					0.0F, bitmap2.getHeight(), -2130706433, 16777215,
					android.graphics.Shader.TileMode.CLAMP);
			Paint paint = new Paint();
			paint.setShader(lineargradient);
			paint.setXfermode(new PorterDuffXfermode(
					android.graphics.PorterDuff.Mode.DST_IN));
			canvas.drawRect(0.0F, 0.0F, j, bitmap2.getHeight(), paint);
			if (!bitmap1.isRecycled())
				bitmap1.recycle();
			System.gc();
		}
		return bitmap2;
	}

	public static Bitmap createReflectedImage(Bitmap bitmap, int i) {
		int j = bitmap.getWidth();
		int k = bitmap.getHeight();
		Bitmap bitmap2;
		if (k <= i) {
			bitmap2 = null;
		} else {
			Matrix matrix = new Matrix();
			matrix.preScale(1.0F, -1F);
			Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, k - i, j, i,
					matrix, true);
			bitmap2 = Bitmap.createBitmap(j, i,
					android.graphics.Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap2);
			canvas.drawBitmap(bitmap1, 0.0F, 0.0F, null);
			LinearGradient lineargradient = new LinearGradient(0.0F, 0.0F,
					0.0F, bitmap2.getHeight(), -2130706433, 16777215,
					android.graphics.Shader.TileMode.CLAMP);
			Paint paint = new Paint();
			paint.setShader(lineargradient);
			paint.setXfermode(new PorterDuffXfermode(
					android.graphics.PorterDuff.Mode.DST_IN));
			canvas.drawRect(0.0F, 0.0F, j, bitmap2.getHeight(), paint);
		}
		return bitmap2;
	}
}