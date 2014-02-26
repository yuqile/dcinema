package cn.leo.dcinema.effect;

import android.graphics.*;

public class Reflect3DImage {

	public Reflect3DImage() {
	}

	public static Bitmap createReflectedImage(Bitmap bitmap, int i) {
		int j = bitmap.getWidth();
		int k = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.preScale(1.0F, -1F);
		Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, k - i, j, i, matrix,
				false);
		Bitmap bitmap2 = Bitmap.createBitmap(j, k + i,
				android.graphics.Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap2);
		Paint paint = new Paint();
		canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
		canvas.drawBitmap(bitmap1, 0.0F, k, paint);
		Paint paint1 = new Paint();
		paint1.setShader(new LinearGradient(0.0F, bitmap.getHeight(), 0.0F,
				bitmap2.getHeight(), 1895825407, 16777215,
				android.graphics.Shader.TileMode.MIRROR));
		paint1.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.DST_IN));
		canvas.drawRect(0.0F, k, j, bitmap2.getHeight(), paint1);
		bitmap1.recycle();
		return bitmap2;
	}

	public static Bitmap skewImage(Bitmap bitmap, int i, int j, int k) {
		Bitmap bitmap1 = createReflectedImage(
				Bitmap.createScaledBitmap(bitmap, i, j, true), k);
		Camera camera = new Camera();
		camera.save();
		Matrix matrix = new Matrix();
		camera.rotateY(15F);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-bitmap1.getWidth() >> 1, -bitmap1.getHeight() >> 1);
		Bitmap bitmap2 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(),
				bitmap1.getHeight(), matrix, true);
		Bitmap bitmap3 = Bitmap.createBitmap(bitmap2.getWidth(),
				bitmap2.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap3);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		canvas.drawBitmap(bitmap2, 0.0F, 0.0F, paint);
		bitmap2.recycle();
		return bitmap3;
	}
}
