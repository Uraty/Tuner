package dnu.ks.barannik.musicpitch.Sound;

public class BufferMutable extends Buffer{

    public BufferMutable(BufferSize size) {
        super(size);
    }
    public BufferMutable(short[] external) {
        super(external);
    }
    public BufferMutable(BufferSize size, float realTimeSeconds) {
        super(size, realTimeSeconds);
    }
    public BufferMutable(short[] external, float realTimeSeconds) {
        super(external, realTimeSeconds);
    }

    public short[] toShort() { return buffer; }
    public void set(int index, short value) { buffer[index] = value; }

    @Override
    protected BufferMutable clone() throws CloneNotSupportedException {
        short[] buffer = this.buffer.clone();
        return new BufferMutable(buffer, realTimeSeconds);
    }
}
