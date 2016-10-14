package dnu.ks.barannik.musicpitch.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnu.ks.barannik.musicpitch.R;
import dnu.ks.barannik.musicpitch.Sound.Buffer;
import dnu.ks.barannik.musicpitch.Sound.Sound;
import dnu.ks.barannik.musicpitch.views.SoundGraphic;
import dnu.ks.barannik.musicpitch.views.SoundSpectreViewer;

public class SpectreFragment extends NamedFragment {

    private SoundSpectreViewer viewer;
    private SoundGraphic graphic;
    {
        name = "Spectrum";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.spectrum_fragment, null);

        graphic = (SoundGraphic) root.findViewById(R.id.Spectrum);
        graphic.initWithSize(512, false);

        return root;
    }

    @Override
    public void onSelect() {
        Sound recorder = Sound.init(Sound.Frequency.f44100, Buffer.BufferSize.b4096);
        recorder.start();
        viewer = new SoundSpectreViewer(recorder, graphic);
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
