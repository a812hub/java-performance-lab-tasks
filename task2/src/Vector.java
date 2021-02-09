import java.util.Objects;

public class Vector {
    final double x;
    final double y;
    final double z;

    public Vector(Point p1, Point p2) {
        this.x = p1.x - p2.x;
        this.y = p1.y - p2.y;
        this.z = p1.z - p2.z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 &&
                Double.compare(vector.y, y) == 0 &&
                Double.compare(vector.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
