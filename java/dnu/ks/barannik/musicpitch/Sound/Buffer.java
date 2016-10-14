package dnu.ks.barannik.musicpitch.Sound;

import android.util.Log;

import java.util.Arrays;
import java.util.Iterator;

public class Buffer implements Cloneable, Iterable<Short> {

    protected short[] buffer;
    protected float realTimeSeconds;

    public float getRealTimeSeconds() {
        return realTimeSeconds;
    }

    protected Buffer(int size, float realTimeSeconds) {
        buffer = new short[size];
        this.realTimeSeconds = realTimeSeconds;
    }

    public Buffer(BufferSize size) {
        buffer = new short[size.getValue()];
    }

    public Buffer(BufferSize size, float realTimeSeconds) {
        this(size);
        this.realTimeSeconds = realTimeSeconds;
    }

    public Buffer(short[] external) {
        buffer = external;

        for (BufferSize size : BufferSize.values())
            if (external.length == size.getValue()) return;
        throw new IllegalArgumentException("Buffer size = " + external.length +
                ". Size must be a power of 2, e.g. 128, 256, 1024, 8192");
    }

    public Buffer(short[] external, float realTimeSeconds) {
        this(external);
        this.realTimeSeconds = realTimeSeconds;
    }

    public short get(int index) {
        return buffer[index];
    }

    public int size() {
        return buffer.length;
    }

    public Buffer Hamming() {
        short[] smoothed = new short[size()];
        int i = 0;
        for (short value : buffer)
            smoothed[i++] = (short) (value *
                    (0.54 - 0.46 * Math.cos(2 * Math.PI * i) / smoothed.length));
        return new Buffer(smoothed, realTimeSeconds);
    }

    public Spectrum FFT() {
        return new Spectrum(
                FFT.apply((buffer)));
    }

    public Frequencies frequencies() {
        return new Frequencies(FFT.magnitude(buffer), 1 / realTimeSeconds);
    }

    public Buffer thinOut(BufferSize power) {
        short[] thinBuffer = new short[size() / power.getValue()];
        for (int i = 0, j = 0; i < thinBuffer.length; i++, j += power.getValue())
            thinBuffer[i] = buffer[j];
        return new Buffer(thinBuffer, realTimeSeconds);
    }
    public Buffer thinOut(float maxFrequency) {
        float currentMax = size() / realTimeSeconds / 2;
        BufferSize factor = BufferSize.less((int) (currentMax / maxFrequency));
        return factor != null? thinOut(factor) : this;
    }

    public Buffer expandWithDuplicates(BufferSize power) {
        short[] expanded = new short[size() * power.getValue()];
        for (int j = 0; j < expanded.length; j += buffer.length)
            for (int i = 0; i < buffer.length; i++)
                expanded[j + i] = buffer[i];
        return new Buffer(expanded, realTimeSeconds * power.getValue());
    }

    public Buffer expand(BufferSize power) {
        short[] expanded = new short[size() * power.getValue()];
        for (int i = 0; i < buffer.length; i++)
            expanded[i] = buffer[i];
        return new Buffer(expanded, realTimeSeconds * power.getValue());
    }

    public Buffer expandForFFTPtrecision(float precisionHz) {
        float currentPrecision = 1 / realTimeSeconds;
        if (currentPrecision > precisionHz) {
            BufferSize factor = BufferSize.larger((int) (currentPrecision / precisionHz));
            return expand(factor);
        }
        return this;
    }

    @Override
    public Iterator<Short> iterator() {
        return new Iterator<Short>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < buffer.length;
            }

            @Override
            public Short next() {
                return buffer[i++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }


    public enum BufferSize {
        b2(2),
        b4(4),
        b8(8),
        b16(16),
        b32(32),
        b64(64),
        b128(128),
        b256(256),
        b512(512),
        b1024(1024),
        b2048(2048),
        b4096(4096),
        b8192(8192),
        b16384(16384),
        b32768(32768),
        b65536(65536);

        private int size;

        BufferSize(int size) {
            this.size = size;
        }

        public int getValue() {
            return size;
        }

        public static BufferSize less(int value) {
            BufferSize[] values = values();
            for (int i = values.length - 1; i >= 0; i--)
                if (values[i].getValue() <= value)
                    return values[i];
            return null;
        }
        public static BufferSize larger(int value) {
            BufferSize[] values = values();
            int i;
            for (i = 0; i < values.length - 1; i++)
                if (values[i].getValue() >= value)
                    return values[i];
            return values[i];
        }
    }
}
