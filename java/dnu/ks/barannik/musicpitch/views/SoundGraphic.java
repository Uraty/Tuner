package dnu.ks.barannik.musicpitch.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import dnu.ks.barannik.musicpitch.FixedBuffer;


interface LineDrawer {
    void draw(Canvas c, float x, float y);
    void reset();
    void setMax(float max);
}

class SimpleLineDrawer implements LineDrawer {

    private float prevX, prevY;
    protected Paint paint = new Paint();

    {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
    }

    private float max;

    @Override
    public void reset() {
        prevX = prevY = 0;
    }

    @Override
    public void setMax(float max) {
        this.max = max;
    }

    @Override
    public void draw(Canvas c, float x, float y) { /*
        c.drawLine(prevX, prevY, x, y, paint);
        prevX = x;
        prevY = y;*/
        c.drawLine(x, max, x, y, paint);
    }
}

class SymmetricDrawer implements LineDrawer {

    protected Paint paint = new Paint();

    {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
    }

    private float max;

    @Override
    public void draw(Canvas c, float x, float y) {
        y /= 2;
        c.drawLine(x, y, x, max - y, paint);
    }

    @Override
    public void reset() {

    }

    @Override
    public void setMax(float max) {
        this.max = max;
    }
}


public class SoundGraphic extends View implements AbstractSoundGraphic {

    public SoundGraphic(Context context) {
        super(context);
    }

    public SoundGraphic(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoundGraphic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected FixedBuffer<Float> points;

    public int getSize() {
        return points.size();
    }
    @Override
    public void initWithSize(int size, boolean symmetric) {
        if (size < 10) size = 10;
        points = new FixedBuffer<>(size);
        points.fillWith(0f);
        lineDrawer = symmetric ? new SymmetricDrawer() : new SimpleLineDrawer();
    }

    @Override
    public void setLevel(float level) {
        points.add(level);
    }

    protected float stepPX, scale = 1f;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        stepPX = (float) w / points.size();
        setRange(max);
    }

    private float max = 20000f;

    public void setRange(float range) {
        max = range;
        lineDrawer.setMax(getHeight());
        scale = getHeight() / range;
    }

    private LineDrawer lineDrawer;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = -stepPX*2;
        for (Float point : points) {
            lineDrawer.draw(canvas, x += stepPX, scale * (max - point));
        }
        lineDrawer.reset();
    }
}
