package dnu.ks.barannik.musicpitch.Sound;

public interface SoundSource {
    SoundSource onRead(OnReadListener... listeners);
    SoundSource unsubscribe(OnReadListener... listeners);
}
