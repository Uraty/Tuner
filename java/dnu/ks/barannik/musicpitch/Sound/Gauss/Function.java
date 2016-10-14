package dnu.ks.barannik.musicpitch.Sound.Gauss;

import java.util.Arrays;
import java.util.Comparator;

public class Function {

    public Function(Point... points) {
        Arrays.sort(this.points = points, new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return Float.compare(lhs.getX(), rhs.getY());
            }
        });
    }

    public float getYLinear(float x) {
        return x < points[0].getX()? points[0].getY() :
                x > points[points.length-1].getY()? points[points.length-1].getY() :
                        linearInterpolation(x);
    }

    private float linearInterpolation(float x) {
        int largerIndex = 0;
        for (Point p : points)
            if (p.getX() > x) break;
            else largerIndex++;
        return  points[largerIndex - 1].getY() +
                (x - points[largerIndex - 1].getX()) /
                (points[largerIndex].getX() - points[largerIndex - 1].getX()) *
                (points[largerIndex].getY() - points[largerIndex - 1].getY());
    }

    private Point[] points;

    public class Point {
        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        private float x, y;

        public Point() {}
        public Point(float x) {
            this.x = x;
        }
        public Point(float x, float y) {

            this.x = x;
            this.y = y;
        }
    }
}
