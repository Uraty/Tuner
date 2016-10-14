package dnu.ks.barannik.musicpitch.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import dnu.ks.barannik.musicpitch.FixedBuffer;

public class ScalableSoundGraphic extends SoundGraphic {

    public ScalableSoundGraphic(Context context) {
        super(context);
    }
    public ScalableSoundGraphic(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ScalableSoundGraphic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float getMin() {
        float min = Float.MAX_VALUE;
        for (Float point : points) {
            if (min > point)
                min = point;
        }
        return min;
    }
    private float getMax() {
        float max = Float.MIN_VALUE;
        for (Float point : points) {
            if (max < point)
                max = point;
        }
        return max;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setRange(getMax() - getMin());
        super.onDraw(canvas);
    }
}
