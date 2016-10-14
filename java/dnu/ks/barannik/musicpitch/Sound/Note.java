package dnu.ks.barannik.musicpitch.Sound;

public class Note {

    public Note(float freq, String name, int number) {
        this.standardFreq = freq;
        this.name = name;
        this.number = number;
    }

    public float getFreq() {
        return standardFreq;
    }

    public String getName() {
        return name;
    }
    public float getNumber() { return number; }

    private float standardFreq;
    private String name;
    private int number;

    @Override
    public String toString() {
        return standardFreq + "Hz - " + name;
    }
}
