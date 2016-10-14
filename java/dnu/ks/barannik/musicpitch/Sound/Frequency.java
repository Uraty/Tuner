package dnu.ks.barannik.musicpitch.Sound;

public class Frequency {

    public Frequency(float frequency, float magnitude) {
        this.frequency = frequency;
        this.magnitude = magnitude;
    }

    public float getFrequency() {
        return frequency;
    }

    public float getMagnitude() {
        return magnitude;
    }

    private float frequency;
    private float magnitude;
}
