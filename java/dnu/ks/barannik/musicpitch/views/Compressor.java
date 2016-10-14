package dnu.ks.barannik.musicpitch.views;

public class Compressor {

    public static float[] average(float[] source, int targetSize) {
        int power = source.length / targetSize;
        if (power < 2) return source;
        float[] result = new float[targetSize];
        for (int i = 0; i < targetSize; i++) {
            float sum = 0;
            for (int j = i * power; j < (i + 1) * power; j++)
                sum += source[j];
            result[i] = sum / power;
        }
        return result;
    }

}
