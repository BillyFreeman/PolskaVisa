package com.victor.polskavisa;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

public class FloatingActionButton extends View {

    private Context context;

    private int bodyColor;
    private Bitmap bitmap;
    private Paint bodyPaint;
    private Paint bitmapPaint;
    private Rect src;
    private RectF dst;

    private RelativeLayout.LayoutParams params;
    private float scale;

    private boolean hiden = true;


    public FloatingActionButton(Context context, Drawable actionImg, int bodyColor) {
        super(context);
        this.context = context;
        this.bitmap = ((BitmapDrawable) actionImg).getBitmap();
        this.bodyColor = bodyColor;
        this.scale = getResources().getDisplayMetrics().density;

        setDefaultLayoutParams();
        init();
    }

    public FloatingActionButton(Context context) {
        this(context, null, Color.YELLOW);
    }

    public FloatingActionButton(Context context, int bodyColor) {
        this(context, null, bodyColor);
    }

    private void setDefaultLayoutParams() {
        int size = dpToPixels(scale, 72);
        params = new RelativeLayout.LayoutParams(size, size);
        params.setMargins(10, 10, 10, 10);
    }

    private void init() {
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        bodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bodyPaint.setColor(this.bodyColor);
        bodyPaint.setStyle(Paint.Style.FILL);
        bodyPaint.setShadowLayer(10.0f, 0.0f, 3.5f, Color.argb(100, 0, 0, 0));
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setClickable(true);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), bodyPaint);
        src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dst = new RectF();
        dst.left = getWidth() * .3f;
        dst.top = getHeight() * .3f;
        dst.right = dst.left + getWidth() * .4f;
        dst.bottom = dst.top + getHeight() * .4f;
        if (bitmap != null)
            canvas.drawBitmap(bitmap, src, dst, bitmapPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            setAlpha(1.0f);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setAlpha(0.6f);
        }
        return super.onTouchEvent(event);
    }

    private int dpToPixels(float scale, int dp) {
        return (int) (scale * dp + .5f);
    }

    public void create(RelativeLayout rl) {
        rl.addView(this, params);
    }

    public void setMargins(int l, int t, int r, int b) {
        params.setMargins(l, t, r, b);
    }

    public void setAlign(int align) {
        params.addRule(align);
    }

    public boolean isHiden() {
        return hiden;
    }

    public void setSize(int size){
        params.height = dpToPixels(scale, size);
        params.width = dpToPixels(scale, size);
        init();
    }

    public void setBodyColor(int bodyColor) {
        this.bodyColor = bodyColor;
        init();
    }


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        init();
    }

    public void setHiden(boolean hiden) {
        this.hiden = hiden;
    }

    public void showAnim(int duration){
        hiden = false;
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(this, "scaleX", 0, 1), ObjectAnimator.ofFloat(this, "scaleY", 0, 1));
        set.setInterpolator(new LinearInterpolator());
        set.setDuration(duration);
        set.start();
    }

    public void hideAnim(int duration) {
        hiden = true;
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(this, "scaleX", 1, 0), ObjectAnimator.ofFloat(this, "scaleY", 1, 0));
        set.setInterpolator(new LinearInterpolator());
        set.setDuration(duration);
        set.start();
    }

}
