import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Point {
    final double x;
    final double y;
    final double z;

    public static Point parsePoint(String s) throws PointParseException {
        Pattern pattern = Pattern.compile("(?<x>-?\\d+\\.*\\d*), (?<y>-?\\d+\\.*\\d*), (?<z>-?\\d+\\.*\\d*)");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            double x = Double.parseDouble(matcher.group("x"));
            double y = Double.parseDouble(matcher.group("y"));
            double z = Double.parseDouble(matcher.group("z"));
            return new Point(x, y, z);
        } else {
            throw new PointParseException("Can't parse point in [" + s + "]");
        }
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0 &&
                Double.compare(point.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
