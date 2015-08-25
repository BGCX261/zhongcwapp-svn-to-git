package com.zhongcw.ui.widget.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

public class LoadingCarToonView extends ImageView {
	public Handler handler = new Handler();

	public LoadingCarToonView(Context context) {
		super(context);
		init();
	}

	public LoadingCarToonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		AnimationDrawable ad = new AnimationDrawable();
		for (int i = 1; i < 13; i++) {
			ad.addFrame(ImgFactory.getDraw( "loading_" + i + ".png",true), 200);
			ad.setOneShot(false);
		}
		this.setBackgroundDrawable(ad);
		this.startcartoon();
	}
	
	public void startcartoon(){
		handler.postDelayed(new Runnable() {
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) getBackground();
				frameAnimation.start();
			}
		}, 200);
	}

	
}
