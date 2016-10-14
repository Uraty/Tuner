package dnu.ks.barannik.musicpitch;

import java.util.Collection;
import java.util.LinkedList;

public class SoundRecordService {

    private static SoundRecordService instance;

    public static SoundRecordService getInstance() throws NotSupportedException {
        if (instance == null)
            synchronized (SoundRecordService.class) {
                if (instance == null)
                    instance = new SoundRecordService();
            }
        return instance;
    }


    private short[] buffer;
    private SoundReader reader;

    private SoundRecordService() throws NotSupportedException {
        reader = new SoundReader();
    }

    private Collection<OnSoundReadedListener> listeners = new LinkedList<>();

    public void subscribe(OnSoundReadedListener listener) {
        listeners.add(listener);
    }

    public void describe(OnSoundReadedListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(short[] buffer, int length) {
        for (OnSoundReadedListener l : listeners)
            l.onRead(buffer, length);
    }

    public boolean recordOn;

    public void start() {
        reader.start();
        recordOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (recordOn) {
                    int length = read();
                    notifyListeners(buffer, length);
                }
            }
        }).start();
    }

    public void stop() {
        recordOn = false;
        reader.end();
    }

    public void release() {
        stop();
        reader.release();
    }

    public int getBufferSize() {
        return reader.getBufferSize();
    }

    private int read() {
        buffer = reader.read();
        return buffer.length;
    }
}
