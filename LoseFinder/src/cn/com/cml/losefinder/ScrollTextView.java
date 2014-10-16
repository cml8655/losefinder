package cn.com.cml.losefinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 实现数字叠加滚动
 * 
 * @author teamlab
 *
 */
public class ScrollTextView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder holder;
	private Paint paint;

	public ScrollTextView(Context context) {
		super(context);
		holder = getHolder();
		holder.addCallback(this);
	}

	public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		holder = getHolder();
		holder.addCallback(this);
	}

	public ScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		holder = getHolder();
		holder.addCallback(this);
	}

	public void draw(int i) {
		Canvas canvas = holder.lockCanvas();
		canvas.drawColor(Color.GREEN);
		canvas.drawText("" + i, getWidth() / 2, getHeight() / 2, defaultPaint());
		holder.unlockCanvasAndPost(canvas);
	}

	private Paint defaultPaint() {
		if (null == paint) {
			paint = new Paint();
			paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			paint.setColor(Color.WHITE);
			paint.setTextSize(100);
			paint.setTextAlign(Paint.Align.CENTER);
		}
		return paint;
	}

	/**
	 * 将数字从start增加到end
	 * 
	 * @param start
	 * @param end
	 * @param duration
	 *            执行时间 ms
	 */
	public void updateText(int start, int end, long duration) {
		long dd = System.currentTimeMillis();

		if (end < start) {
			return;
		}
		
		if (end == start) {
			draw(end);
			return;
		}

		int increment = end - start;

		int interval = (int) (duration / increment);

		Log.d("dddd", "分隔时间：" + interval + "," + increment + "," + duration
				+ ":" + (interval * increment));

		for (int i = start + 1; i <= end; i++) {
			try {
				draw(i);
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Log.d("dddd", "执行时间:" + (System.currentTimeMillis() - dd));
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Canvas canvas = holder.lockCanvas();
		// canvas.drawColor(Color.GREEN);
		// holder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

}
