package dnu.ks.barannik.musicpitch.Sound;

import android.util.Log;

public class NoteSystem {

    public NoteSystem() {
        LA = 440f / 16;
    }
    public NoteSystem(float laFreq) {
        LA = laFreq / 16;
    }

    public float getNoteNumber (float frequency) {
        return frequency <= LA? 0 : log(LOG_BASE, frequency / LA);
    }

    public static final String[] NOTE_NAMES = {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

    public Note getNote(int number) {
        return new Note(getFreq(number), NOTE_NAMES[(number + 9) % 12], number);
    }

    private final float LA;
    private final float LOG_BASE = (float) Math.pow(2, 1 / 12f);

    private float log(float base, float result) {
        return (float) (Math.log(result) / Math.log(base));
    }
    private float getFreq(float noteNumber) {
        return (float) (LA * Math.pow(LOG_BASE, noteNumber));
    }
}
