package dnu.ks.barannik.musicpitch.Sound;

import android.media.AudioFormat;
import android.media.AudioRecord;

import android.media.MediaRecorder.*;

import java.util.Collection;
import java.util.LinkedList;


public class Sound implements SoundSource {

    private static Sound instance;
    private Sound(AudioRecord source, Buffer.BufferSize bufferSize) {
        this.audioSource = source;
        float bufferTimeSeconds = (float) bufferSize.getValue() / source.getSampleRate();
        this.buffer = new BufferMutable(bufferSize, bufferTimeSeconds);
        this.reader = new Reader();
    }

    public static synchronized Sound init(Frequency f, Buffer.BufferSize size) {
        if (instance == null) {
            AudioRecord source = new AudioRecord(
                    AudioSource.DEFAULT, f.getValue(),
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    size.getValue() * 2);
            if (source.getState() == AudioRecord.STATE_INITIALIZED)
                instance = new Sound(source, size);
        }
        return instance;
    }

    public static Sound init(Buffer.BufferSize size) {
        for (Frequency f : Frequency.values())
            if (init(f, size) != null)
                break;
        return instance;
    }

    public static Sound init() {
        for (Buffer.BufferSize size : Buffer.BufferSize.values())
            if (init(size) != null)
                break;
        return instance;
    }

    public static synchronized void release() {
        if (instance != null) {
            instance.stop();
            instance.audioSource.release();
            instance = null;
        }
    }

    public Sound start() {
        if (!started) {
            started = true;
            if (listeners.size() > 0) reader.start();
        }
        return this;
    }
    public Sound stop() {
        if (started) {
            started = false;
            reader.stop();
        }
        return this;
    }

    private Collection<OnReadListener> listeners = new LinkedList<>();
    @Override
    public Sound onRead(OnReadListener... listeners) {
        synchronized (this.listeners) {
            for (OnReadListener l : listeners)
                this.listeners.add(l);
            if (started && audioSource.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING)
                reader.start();
        }
        return this;
    }
    @Override
    public SoundSource unsubscribe(OnReadListener... listeners) {
        synchronized (this.listeners) {
            for (OnReadListener l : listeners)
                this.listeners.remove(l);
            if (this.listeners.size() == 0 &&
                    audioSource.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING)
                reader.stop();
        }
        return this;
    }

    public int getBufferSize() {
        return buffer.size();
    }

    private AudioRecord audioSource;
    private boolean started;
    private BufferMutable buffer;
    private Reader reader;

    private class Reader{
        private boolean run;
        public void start() {
            run = true;
            synchronized (audioSource) {
                audioSource.startRecording();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (run) {
                        synchronized (audioSource) {
                            if (run)
                                audioSource.read(buffer.toShort(), 0, buffer.size());
                        }
                        synchronized (listeners) {
                            for (OnReadListener l : listeners) l.onRead(buffer);
                        }
                    }
                }
            }).start();
        }
        public void stop() {
            run = false;
            synchronized (audioSource) {
                audioSource.stop();
            }
        }
    }

    public enum Frequency {
        f44100(44100),
        f22050(22050),
        f16000(16000),
        f11025(11025),
        f8000(8000);

        private int frequency;
        Frequency(int f) {
            frequency = f;
        }
        public int getValue() {
            return frequency;
        }
    }
}
