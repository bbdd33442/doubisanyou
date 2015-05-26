package com.doubisanyou.appcenter.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.doubisanyou.appcenter.R;


public class CircularImageView extends ImageView {
	private int mBorderThickness = 0;
	private Context mContext;
	private int mBorderColor = 0xFFFFFFFF;
	private Paint paint = new Paint();

	public CircularImageView(Context context) {
		super(context);
		mContext = context;
	}

	public CircularImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setCustomAttributes(attrs);
	}

	public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		setCustomAttributes(attrs);
	}

	private void setCustomAttributes(AttributeSet attrs) {
		if (attrs == null)
			return;

		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.circularimageview);
		mBorderThickness = a.getDimensionPixelSize(R.styleable.circularimageview_border_thickness, 0);
		mBorderColor = a.getColor(R.styleable.circularimageview_border_color, mBorderColor);

		a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		this.measure(0, 0);
		if (drawable.getClass() == NinePatchDrawable.class)
			return;
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

		int w = getWidth(), h = getHeight();

		int radius = (w < h ? w : h) / 2 - mBorderThickness;
		Bitmap roundBitmap = roundCorners(bitmap, radius);

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(mBorderColor);

		canvas.drawCircle(w / 2, h / 2, radius + mBorderThickness, paint);
		canvas.drawBitmap(roundBitmap, w / 2 - radius, h / 2 - radius, null);

	}

	private Bitmap roundCorners(Bitmap bitmap, int roundPixels) {

		Bitmap roundBitmap;

		int bw = bitmap.getWidth();
		int bh = bitmap.getHeight();
		int vw = getWidth();
		int vh = getHeight();
		if (vw <= 0)
			vw = bw;
		if (vh <= 0)
			vh = bh;

		final ImageView.ScaleType scaleType = getScaleType();
		if (scaleType == null) {
			return bitmap;
		}

		int width, height;
		Rect srcRect;
		Rect destRect;
		switch (scaleType) {
		case CENTER_INSIDE:
			float vRation = (float) vw / vh;
			float bRation = (float) bw / bh;
			int destWidth;
			int destHeight;
			if (vRation > bRation) {
				destHeight = Math.min(vh, bh);
				destWidth = (int) (bw / ((float) bh / destHeight));
			} else {
				destWidth = Math.min(vw, bw);
				destHeight = (int) (bh / ((float) bw / destWidth));
			}
			int x = (vw - destWidth) / 2;
			int y = (vh - destHeight) / 2;
			srcRect = new Rect(0, 0, bw, bh);
			destRect = new Rect(x, y, x + destWidth, y + destHeight);
			width = vw;
			height = vh;
			break;
		case FIT_CENTER:
		case FIT_START:
		case FIT_END:
		default:
			vRation = (float) vw / vh;
			bRation = (float) bw / bh;
			if (vRation > bRation) {
				width = (int) (bw / ((float) bh / vh));
				height = vh;
			} else {
				width = vw;
				height = (int) (bh / ((float) bw / vw));
			}
			srcRect = new Rect(0, 0, bw, bh);
			destRect = new Rect(0, 0, width, height);
			break;
		case CENTER_CROP:
			vRation = (float) vw / vh;
			bRation = (float) bw / bh;
			int srcWidth;
			int srcHeight;
			if (vRation > bRation) {
				srcWidth = bw;
				srcHeight = (int) (vh * ((float) bw / vw));
				x = 0;
				y = (bh - srcHeight) / 2;
			} else {
				srcWidth = (int) (vw * ((float) bh / vh));
				srcHeight = bh;
				x = (bw - srcWidth) / 2;
				y = 0;
			}
			width = vw;// Math.min(vw, bw);
			height = vh;// Math.min(vh, bh);
			srcRect = new Rect(x, y, x + srcWidth, y + srcHeight);
			destRect = new Rect(0, 0, width, height);
			break;
		case FIT_XY:
			width = vw;
			height = vh;
			srcRect = new Rect(0, 0, bw, bh);
			destRect = new Rect(0, 0, width, height);
			break;
		case CENTER:
		case MATRIX:
			width = Math.min(vw, bw);
			height = Math.min(vh, bh);
			x = (bw - width) / 2;
			y = (bh - height) / 2;
			srcRect = new Rect(x, y, x + width, y + height);
			destRect = new Rect(0, 0, width, height);
			break;
		}

		try {
			roundBitmap = getRoundedCornerBitmap(bitmap, roundPixels, srcRect, destRect, width, height);
		} catch (OutOfMemoryError e) {
			roundBitmap = bitmap;
		} catch (Exception e) {
			roundBitmap = bitmap;
		}

		return roundBitmap;
	}

	private Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPixels, Rect srcRect, Rect destRect, int width,
			int height) {
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final RectF destRectF = new RectF(destRect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(0xFF000000);
		canvas.drawRoundRect(destRectF, roundPixels, roundPixels, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, srcRect, destRectF, paint);

		return output;
	}
}