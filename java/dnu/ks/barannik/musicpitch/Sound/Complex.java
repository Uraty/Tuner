package dnu.ks.barannik.musicpitch.Sound;

public class Complex  {

    public float getReal() {
        return real;
    }

    public float getImagine() {
        return imagine;
    }

    protected float real, imagine;

    public Complex() {
    }

    public Complex(float real) {
        this.real = real;
    }

    public Complex(float real, float imagine) {
        this.real = real;
        this.imagine = imagine;
    }

    public Complex(double real, double imagine) {
        this.real = (float) real;
        this.imagine = (float) imagine;
    }

    public static final Complex zero = new Complex(0);
    public static Complex[] toComplex(short[] values) {
        Complex[] result = new Complex[values.length];
        int i = 0;
        for (short v : values)
            result[i++] = v == 0? zero : new Complex(v);
        return result;
    }
    public static Complex[] toComplex(float[] real, float[] imagine) {
        if (real.length != imagine.length)
            throw new IllegalArgumentException(
                    "Length of real array doesn't match length of imagine part");
        Complex[] result = new Complex[real.length];
        for (int i = 0; i < result.length; i++)
            result[i] = new Complex(real[i], imagine[i]);
        return result;
    }

    public Complex add(double real) {
        return new Complex(real + this.real, imagine);
    }

    public Complex add(Complex other) {
        return new Complex(other.real + this.real, other.imagine + this.imagine);
    }
    public Complex deduct(Complex other) {
        return new Complex(this.real - other.real, this.imagine - other.imagine);
    }

    public Complex multiply(Complex other) {
        return new Complex(this.real * other.real - this.imagine * other.imagine,
                this.imagine * other.real + this.real * other.imagine);
    }

    public float abs() {
        return (float) Math.sqrt(real * real + imagine * imagine);
    }

    @Override
    public String toString() {
        return real + (imagine == 0? "" :
                ((imagine > 0? " + " : " - ") + Math.abs(imagine) + "i"));
    }
}
