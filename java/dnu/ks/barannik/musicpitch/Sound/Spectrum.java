package dnu.ks.barannik.musicpitch.Sound;

public class Spectrum {

    private Complex[] result;

    public Spectrum(Complex[] result) {
        this.result = result;
    }

    public float[] magnitude() {
        float[] magnitude = new float[result.length / 2];
        for (int i = 0; i < magnitude.length; i++) {
            magnitude[i] = result[i].abs();
        }
        return magnitude;
    }

    public Complex[] toComplex() {
        return result;
    }
}
