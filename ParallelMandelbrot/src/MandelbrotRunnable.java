
import org.apache.commons.math3.complex.Complex;

public class MandelbrotRunnable implements Runnable {
    private short[][] mat;
    private int g;
    private int height;
    private int width;
    private double startX;
    private double endX;
    private double startY;
    private double endY;
    private int number;
    private int thread_count;

    MandelbrotRunnable(short[][] mat, int g, int height, int width, double startX, double endX, double startY, double endY, int number, int thread_count) {
        this.mat = mat;
        this.g = g;
        this.height = height;
        this.width = width;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.number = number;
        this.thread_count = thread_count;
    }

    public static Complex z_iter(Complex z, Complex c) {
        return z.multiply(z).add(c);
    }

    public static short z_check(Complex c) {

        Complex z0 = new Complex(0.0, 0.0);

        Complex z_prev = z0;
        Complex z_i = null;

        short steps = 0;

        Double d = null;

        for(short i = 0; i < 256; i++) {

            z_i = z_iter(z_prev, c);
            z_prev = z_i;

            d = z_prev.getReal();

            if (d.isInfinite() || d.isNaN()) {

                steps = i;
                break;

            }

        }

        return steps;
    }

    public long _gts() {
        return System.nanoTime();
    }

    @Override
    public void run() {
        int N = thread_count*g;
        int d = height/N;
        int k = this.number*d;

        //long _ts_b = _gts();

        while (k < height) {
            for (int l = 0; l < d; l++) {
                for (int j = 0; j < (int) (width); j++) {
                    double px = startX + (j * 1.0 / width) * (endX - startX);
                    double py = startY + (k * 1.0 / height) * (endY - startY);
                    short r = z_check(new Complex(px, py));
                    mat[k][j] = r;
                }
                k++;
                if (k >= height) {
                    break;
                }
            }
            k+=(thread_count-1)*d;
        }

        //long _ts_e = _gts();

        //double _total_time = (_ts_e - _ts_b) / 1000000.0;

        //System.out.println("End of thread " + number + ": total time (millis) -> " + _total_time);

    }
}
