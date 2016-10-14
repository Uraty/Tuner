package dnu.ks.barannik.musicpitch;

public interface OnSoundReadedListener {
    void onRead(short[] buffer, int length);
}
