package dnu.ks.barannik.musicpitch.Sound;

import java.util.ArrayList;
import java.util.Collection;

public class Frequencies {

    public Frequencies(float[] values, float frequencyStep) {
        this.values = values;
        this.frequencyStep = frequencyStep;
    }

    public float[] getValues() {
        return values;
    }

    public float getFrequencyStep() {
        return frequencyStep;
    }

    public Frequency getMax() {
        int maxFreqIndex = getMaxFreqIndex(values);
        return new Frequency(maxFreqIndex * frequencyStep, values[maxFreqIndex]);
    }

    public ArrayList<Frequency> getAllMax() {
        return getAllMax(0.5f);
    }
    public ArrayList<Frequency> getAllMax(float gainLevel) {
        float maxFreq = 0;
        for (float value : values)
            if (maxFreq < value) maxFreq = value;
        float level = maxFreq * gainLevel;

        ArrayList<Frequency> result = new ArrayList<>();
        float[] values = this.values.clone();
        while (true) {
            int maxIndex = getMaxFreqIndex(values);
            if (values[maxIndex] < level) return result;
            result.add(new Frequency(maxIndex * frequencyStep, values[maxIndex]));
            suppress(values, maxIndex, 30);
        }
    }

    private int getMaxFreqIndex(float[] values) {
        int maxIndex = 0;
        float max = Float.MIN_VALUE;
        int i = 0;
        for (float value : values) {
            if (value > max) {
                max = value;
                maxIndex = i;
            }
            i++;
        }
        return maxIndex;
    }

    private void suppress(float[] values, int index, int width) {
        int startIndex = index - width / 2;
        if (startIndex < 0) startIndex = 0;
        int endIndex = index + width / 2;
        if (endIndex > values.length) endIndex = values.length;

        for (int i = startIndex; i < endIndex; i++) {
            values[i] = 0;
        }
    }

    private float[] values;
    private float frequencyStep;
}
