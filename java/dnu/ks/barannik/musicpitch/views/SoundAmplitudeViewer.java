package dnu.ks.barannik.musicpitch.views;

import android.util.Log;

import dnu.ks.barannik.musicpitch.FFT;
import dnu.ks.barannik.musicpitch.OnSoundReadedListener;
import dnu.ks.barannik.musicpitch.Sound.Buffer;
import dnu.ks.barannik.musicpitch.Sound.Complex;
import dnu.ks.barannik.musicpitch.Sound.Frequencies;
import dnu.ks.barannik.musicpitch.Sound.OnReadListener;
import dnu.ks.barannik.musicpitch.Sound.Sound;
import dnu.ks.barannik.musicpitch.SoundRecordService;
import dnu.ks.barannik.musicpitch.views.SoundGraphic;

public class SoundAmplitudeViewer implements OnReadListener {

    private Sound recorder;
    private SoundGraphic view;

    public SoundAmplitudeViewer(Sound recorder, SoundGraphic view) {
        this.recorder = recorder;
        this.view = view;
    }

    public void start() {
        recorder.onRead(this);
    }

    public void stop() {
        recorder.unsubscribe(this);
    }


    @Override
    public void onRead(Buffer buffer) {
        float[] values = new float[buffer.size()];
        int i = 0;
        for (short b : buffer)
            values[i++] = b;
        for (float v : Compressor.average(values, 100))
            view.setLevel(v);
        view.postInvalidate();
    }
}
