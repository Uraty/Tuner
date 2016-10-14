package dnu.ks.barannik.musicpitch.Sound;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class FFT {
    private static final double DoublePi = 2*Math.PI;

    public static Complex[] apply(short[] values) {
        float[] floatValues = new float[values.length];
        int i = 0;
        for (short v : values) floatValues[i++] = v;
        return apply(floatValues);
    }
    public static Complex[] apply(float[] values) {
        float[] valuesIm = new float[values.length],
                resultRl = new float[values.length],
                resultIm = new float[values.length];
        apply(values, valuesIm, resultRl, resultIm);
        return Complex.toComplex(resultRl, resultIm);
    }

    public static float[] magnitude(short[] values) {
        float[] floatValues = new float[values.length];
        int i = 0;
        for (short v : values) floatValues[i++] = v;
        return magnitude(floatValues);
    }
    public static float[] magnitude(float[] values) {
        float[] freq = new float[values.length/2];
        float[] valuesIm = new float[values.length],
                resultRl = new float[values.length],
                resultIm = new float[values.length];
        apply(values, valuesIm, resultRl, resultIm);
        for (int i = 0; i < freq.length; i++)
            freq[i] = (float) Math.sqrt(resultRl[i] * resultRl[i] + resultIm[i] * resultIm[i]);
        return freq;
    }

    private static void apply(float[] frameReal, float[] frameImagine,
                             float[] spectrumRl, float[] spectrumIm)
    {
        if (frameReal.length == 1) {
            spectrumRl[0] = frameReal[0];
            spectrumIm[0] = frameImagine[0];
            return;
        }
        int frameHalfSize = frameReal.length >> 1; // frame.Length/2
        int frameFullSize = frameReal.length;

        float[] frameOddRl = new float[frameHalfSize],
                frameOddIm = new float[frameHalfSize];
        float[] frameEvenRl = new float[frameHalfSize],
                frameEvenIm = new float[frameHalfSize];
        for (int i = 0; i < frameHalfSize; i++)
        {
            int j = i << 1; // i = 2*j;
            frameOddRl[i] = frameReal[j + 1];
            frameOddIm[i] = frameImagine[j + 1];
            frameEvenRl[i] = frameReal[j];
            frameEvenIm[i] = frameImagine[j];
        }

        float[] spectrumOddRl = new float[frameHalfSize],
                spectrumOddIm = new float[frameHalfSize];
        float[] spectrumEvenRl = new float[frameHalfSize],
                spectrumEvenIm = new float[frameHalfSize];
        apply(frameOddRl, frameOddIm, spectrumOddRl, spectrumOddIm);
        apply(frameEvenRl, frameEvenIm, spectrumEvenRl, spectrumEvenIm);

        double arg = -DoublePi/frameFullSize;
        float omegaPowBaseRl = (float) Math.cos(arg),
                omegaPowBaseIm = (float) Math.sin(arg);
        float omegaRl = 1, omegaIm = 0;

        for (int j = 0; j < frameHalfSize; j++) {
            spectrumRl[j] = spectrumEvenRl[j] +
                    omegaRl * spectrumOddRl[j] - omegaIm * spectrumOddIm[j];
            spectrumIm[j] = spectrumEvenIm[j] +
                    omegaRl * spectrumOddIm[j] + omegaIm * spectrumOddRl[j];

            spectrumRl[j + frameHalfSize] = spectrumEvenRl[j] -
                    omegaRl * spectrumOddRl[j] + omegaIm * spectrumOddIm[j];
            spectrumIm[j + frameHalfSize] = spectrumEvenIm[j] -
                    omegaRl * spectrumOddIm[j] - omegaIm * spectrumOddRl[j];

            float newOmegaRl = omegaPowBaseRl * omegaRl - omegaPowBaseIm * omegaIm;
            omegaIm = omegaPowBaseRl * omegaIm + omegaPowBaseIm * omegaRl;
            omegaRl = newOmegaRl;
        }
    }
}
