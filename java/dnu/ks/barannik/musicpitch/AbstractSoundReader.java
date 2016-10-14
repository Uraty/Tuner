package dnu.ks.barannik.musicpitch;

public interface AbstractSoundReader {
    short[] read();
    int getBufferSize();
}
