package cn.com.cml.losefinder.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RadarTranslateView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder holder;

	// 声明渐变的颜色数组
	private static final int[] color = new int[] { Color.parseColor("#1A54B8"),
			Color.GREEN, Color.CYAN };

	private static final int[] bgColor = new int[] {
			Color.parseColor("#55F4FD"), Color.parseColor("#034DAD"),
			Color.parseColor("#1A54B8"), Color.parseColor("#60C7F6") };

	private boolean allowDraw = true;

	private static final String text = "正在追踪...";

	public RadarTranslateView(Context context) {
		super(context);
		init();
	}

	private void init() {
		holder = this.getHolder();
		holder.addCallback(this);
	}

	public RadarTranslateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RadarTranslateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new SurfaceThread().start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		allowDraw = false;
	}

	private class SurfaceThread extends Thread {

		private void drawCircle(float cx, float cy, float radius, int levels,
				Canvas canvas, Paint paint) {

			Paint p = new Paint(paint);
			p.setStyle(Paint.Style.STROKE);
			p.setColor(Color.parseColor("#1B6C0F"));

			int averageLevel = Math.round(radius / levels);

			while (levels > 0 && allowDraw) {
				levels--;
				RectF rect = new RectF(cx - radius + averageLevel * levels, cy
						- radius + averageLevel * levels, cx + radius
						- averageLevel * levels, cy + radius - averageLevel
						* levels);
				canvas.drawArc(rect, 0, 360, true, p);
			}
		}

		@Override
		public void run() {
			int i = 0;
			int radius = getWidth() / 2 - 10;
			int width = getWidth();
			int cx = width / 2;
			int cy = width / 2;
			Paint paint = new Paint();
			paint.setAntiAlias(true);

			while (true && allowDraw) {

				Canvas canvas = holder.lockCanvas();

				Shader mShader = new RadialGradient(cx, cy, radius, bgColor,
						null, TileMode.MIRROR);

				paint.setShader(mShader);

				canvas.drawColor(Color.WHITE);// 蓝色背景
				paint.setColor(Color.parseColor("#003803"));
				canvas.drawCircle(cx, cy, radius, paint);

				paint.setShader(null);
				paint.setColor(Color.GREEN);
				paint.setTextSize(60);
				paint.setTypeface(Typeface.SERIF);

				// paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
				paint.setColor(Color.parseColor("#44973D"));

				// 画叠加的元
				drawCircle(cx, cy, radius, 5, canvas, paint);
				paint.setStrokeWidth(6);

				mShader = new SweepGradient(width, width, color, null);

				paint.setShader(mShader);

				RectF oval = new RectF(0, 0, width, width);

				canvas.drawArc(oval, i, 30, true, paint);

				// 设置文本
				canvas.drawText(text, cx - paint.measureText(text) / 2, cy,
						paint);

				i += 15;
				if (i > 360) {
					i = 0;
				}

				holder.unlockCanvasAndPost(canvas);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
