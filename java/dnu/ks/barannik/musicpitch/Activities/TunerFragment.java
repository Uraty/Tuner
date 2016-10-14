package dnu.ks.barannik.musicpitch.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dnu.ks.barannik.musicpitch.R;
import dnu.ks.barannik.musicpitch.Sound.Buffer;
import dnu.ks.barannik.musicpitch.Sound.Frequencies;
import dnu.ks.barannik.musicpitch.Sound.Frequency;
import dnu.ks.barannik.musicpitch.Sound.Note;
import dnu.ks.barannik.musicpitch.Sound.NoteSystem;
import dnu.ks.barannik.musicpitch.Sound.OnReadListener;
import dnu.ks.barannik.musicpitch.Sound.Sound;

public class TunerFragment extends NamedFragment implements OnReadListener {

    {
        name = "Tuner";
    }

    private TextView up, down, cur;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.tuner_fragment, null);

        up = (TextView) root.findViewById(R.id.topNote);
        down = (TextView) root.findViewById(R.id.bottomNote);
        cur = (TextView) root.findViewById(R.id.curNote);

        return root;
    }

    private Sound recorder;
    @Override
    public void onSelect() {
        recorder = Sound.init(Sound.Frequency.f44100, Buffer.BufferSize.b8192);
        recorder.start();
        recorder.onRead(this);
    }

    @Override
    public void onDeselect() {
        if (recorder != null)
            recorder.unsubscribe(this);
        Sound.release();
    }

    private String noteDescription(Note note) {
        return String.format("%2s\n%3.1fHz", note.getName(), note.getFreq());
    }

    private float position(float relativePos) {
        float delta = down.getY() - up.getY();
        return up.getY() + delta * relativePos;
    }

    @Override
    public void onRead(Buffer buffer) {
        Frequencies f = buffer.thinOut(500).expandForFFTPtrecision(0.5f).Hamming().frequencies();
        List<Frequency> frequencies = f.getAllMax();
        Collections.sort(frequencies, new Comparator<Frequency>() {
            @Override
            public int compare(Frequency lhs, Frequency rhs) {
                return Float.compare(lhs.getFrequency(), rhs.getFrequency());
            }
        });

        final NoteSystem notes = new NoteSystem();
        Frequency main;
        if (frequencies.size() <= 1)
            main = frequencies.get(0);
        else {
            float interval = Math.round(notes.getNoteNumber(frequencies.get(1).getFrequency()) -
                    notes.getNoteNumber(frequencies.get(0).getFrequency()));
            main = interval == 5? frequencies.get(1) : frequencies.get(0);
        }


        final float freq = main.getFrequency();
        final float noteNumber = notes.getNoteNumber(freq);

        cur.post(new Runnable() {
            @Override
            public void run() {
                cur.setY(position(noteNumber - (int) noteNumber));
                cur.setText(String.format("%3.1fHz", freq));

                Note upNote = notes.getNote((int) noteNumber),
                        downNote = notes.getNote((int) noteNumber + 1);
                up.setText(noteDescription(upNote));
                down.setText(noteDescription(downNote));
            }
        });
    }
}
