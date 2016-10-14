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

public class SoundSpectreViewer implements OnReadListener {

    private Sound recorder;
    private SoundGraphic view;

    public SoundSpectreViewer(Sound recorder, SoundGraphic view) {
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
        Frequencies f = buffer.thinOut(500).expandForFFTPtrecision(0.5f).Hamming().frequencies();
        for (float m : Compressor.average(f.getValues(), view.getSize()))
            view.setLevel(m);
        view.postInvalidate();
    }
}
