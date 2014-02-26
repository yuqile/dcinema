package cn.leo.dcinema.effect;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationSetUtils {
	static int num = 0;

	public static class TextViewAnimation implements
			Animation.AnimationListener {
		private String info = null;
		private String temp = null;
		private TextView v = null;

		public TextViewAnimation(String info, String temp, TextView v) {
			this.info = info;
			this.temp = temp;
			this.v = v;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			AnimationSetUtils.num = 1 + AnimationSetUtils.num;
			if (AnimationSetUtils.num == 10)
				AnimationSetUtils.num = 0;
			if (AnimationSetUtils.num % 2 == 0)
				v.setText(info);
			else
				v.setText(temp);
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}
	}

	public static class ImageViewAnimation implements
			Animation.AnimationListener {
		private int bg1 = 0;
		private int bg2 = 0;
		private ImageView imageV = null;

		public ImageViewAnimation(int bg1, int bg2, ImageView imageV) {
			this.bg1 = bg1;
			this.bg2 = bg2;
			this.imageV = imageV;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			AnimationSetUtils.num = 1 + AnimationSetUtils.num;
			if (AnimationSetUtils.num == 10)
				AnimationSetUtils.num = 0;
			if (AnimationSetUtils.num % 2 == 0)
				imageV.setImageResource(bg1);
			else
				imageV.setImageResource(bg2);
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}
	}

	public static void SetFlickerAnimation(final TextView v, final String info,
			final String temp) {
		v.setText(info);
		AlphaAnimation alphaanimation = new AlphaAnimation(0.0F, 1.0F);
		alphaanimation.setDuration(4000L);
		alphaanimation.setRepeatCount(-1);
		alphaanimation
				.setAnimationListener(new TextViewAnimation(info, temp, v));
		v.startAnimation(alphaanimation);
	}

	public static void SetMenuAnimation(final ImageView v, final int bg1,
			final int bg2) {
		v.setImageResource(bg1);
		AlphaAnimation alphaanimation = new AlphaAnimation(0.8F, 1.0F);
		alphaanimation.setDuration(1000L);
		alphaanimation.setRepeatCount(-1);
		alphaanimation.setInterpolator(new AccelerateInterpolator());
		alphaanimation
				.setAnimationListener(new ImageViewAnimation(bg1, bg2, v));
		v.startAnimation(alphaanimation);
	}

	public static Animation createAlphaAnimation(float f, float f1, long l) {
		AlphaAnimation alphaanimation = new AlphaAnimation(f, f1);
		alphaanimation.setDuration(l);
		alphaanimation.setInterpolator(new AccelerateInterpolator());
		return alphaanimation;
	}

	public static Animation createScaleAnimation(float f, float f1, float f2,
			float f3, long l) {
		ScaleAnimation scaleanimation = new ScaleAnimation(f, f1, f2, f3, 1,
				0.5F, 1, 0.5F);
		scaleanimation.setFillAfter(true);
		scaleanimation.setInterpolator(new AccelerateInterpolator());
		scaleanimation.setDuration(l);
		return scaleanimation;
	}

}
