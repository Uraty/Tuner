package dnu.ks.barannik.musicpitch.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnu.ks.barannik.musicpitch.R;
import dnu.ks.barannik.musicpitch.Sound.Buffer;
import dnu.ks.barannik.musicpitch.Sound.Sound;
import dnu.ks.barannik.musicpitch.views.SoundAmplitudeViewer;
import dnu.ks.barannik.musicpitch.views.SoundGraphic;
import dnu.ks.barannik.musicpitch.views.SoundSpectreViewer;

public class AmplitudeFragment extends NamedFragment {

    private SoundGraphic graphic;
    private SoundAmplitudeViewer viewer;

    {
        name = "Amplitude";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.amplitude_fragment, null);

        graphic = (SoundGraphic) root.findViewById(R.id.Amplitude);
        graphic.initWithSize(512, true);

        onSelect();
        return root;
    }

    @Override
    public void onSelect() {
        Sound recorder = Sound.init(Sound.Frequency.f44100, Buffer.BufferSize.b1024);
        recorder.start();
        viewer = new SoundAmplitudeViewer(recorder, graphic);
        viewer.start();
    }

    @Override
    public void onDeselect() {
        if (viewer != null) {
            viewer.stop();
            viewer = null;
        }
        Sound.release();
    }
}
