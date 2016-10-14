package dnu.ks.barannik.musicpitch;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class SoundReader implements AbstractSoundReader{

    private AudioRecord recorder;
    private short[] buffer;
    private final int SAMPLE_RATE = 44100;
    private AudioRecord getRecorder() {
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        return new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize*2);
    }

    private final int DEFAULT_BUFFER_SIZE = 2048;
    public SoundReader() throws NotSupportedException {
        recorder = getRecorder();
        if (recorder.getState() != AudioRecord.STATE_INITIALIZED)
            throw new NotSupportedException("Device doesn't support audio recording");
        buffer = new short[DEFAULT_BUFFER_SIZE];
    }

    @Override
    public short[] read() {
        recorder.read(buffer, 0, buffer.length);
        return buffer;
    }

    @Override
    public int getBufferSize() {
        return buffer.length;
    }

    public void start() {
        recorder.startRecording();
    }
    public void end() {
        recorder.stop();
    }
    public void release() {
        recorder.release();
    }
}
