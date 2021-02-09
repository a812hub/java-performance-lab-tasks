import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println(usage("There must be 1 argument when starting the program"));
        } else {
            try (BufferedReader file = new BufferedReader(new FileReader(args[0]))) {
                String fileData = file.readLine();

                // Parse Data from file.
                int indexOfCenter = fileData.indexOf("center:");
                String centerXYZ = fileData.substring(fileData.indexOf("[", indexOfCenter) + 1, fileData.indexOf("]", indexOfCenter));
                Point centerPoint = Point.parsePoint(centerXYZ);

                double radius = 0;
                Pattern pattern = Pattern.compile("radius: (?<radius>-?\\d+\\.*\\d*)");
                Matcher matcher = pattern.matcher(fileData);
                if (matcher.find()) {
                    radius = Double.parseDouble(matcher.group("radius"));
                }

                int indexOfAPoint = fileData.indexOf("line:");
                String aPointXYZ = fileData.substring(fileData.indexOf("[", indexOfAPoint) + 1, fileData.indexOf("]", indexOfAPoint));
                Point aPoint = Point.parsePoint(aPointXYZ);

                int indexOfBPoint = fileData.indexOf("]", indexOfAPoint) + 1;
                String bPointXYZ = fileData.substring(fileData.indexOf("[", indexOfBPoint) + 1, fileData.indexOf("]", indexOfBPoint));
                Point bPoint = Point.parsePoint(bPointXYZ);

                List<Point> collision = getCollisions(centerPoint, radius, aPoint, bPoint);
                if (collision.isEmpty()) {
                    System.out.println("Коллизий не найдено");
                } else {
                    collision.forEach(System.out::println);
                }

            } catch (FileNotFoundException e) {
                System.out.println(usage("File cannot be found: " + args[0]));
            } catch (IOException e) {
                System.out.println(usage("File reading error. " + e.getMessage()));
            } catch (PointParseException e) {
                System.out.println(usage("Invalid data in the file. " + e.getMessage()));
            }
        }
    }


    private static List<Point> getCollisions(Point center, double radius, Point linePointA, Point linePointB) {
        List<Point> result = new ArrayList<>();

        Vector abVector = new Vector(linePointA, linePointB);
        Vector boVector = new Vector(linePointB, center);

        double aFactor = Math.pow(abVector.x, 2) + Math.pow(abVector.y, 2) + Math.pow(abVector.z, 2);
        double bFactor = 2 * (abVector.x * boVector.x + abVector.y * boVector.y + abVector.z * boVector.z);
        double cFactor = Math.pow(boVector.x, 2) + Math.pow(boVector.y, 2) + Math.pow(boVector.z, 2) - Math.pow(radius, 2);
        double discriminant = bFactor * bFactor - 4 * aFactor * cFactor;
        if (discriminant > 0) {
            double t1 = (-bFactor + Math.sqrt(discriminant)) / (2 * aFactor);
            double t2 = (-bFactor - Math.sqrt(discriminant)) / (2 * aFactor);
            Point resultPoint1 = new Point(abVector.x * t1 + linePointB.x, abVector.y * t1 + linePointB.y, abVector.z * t1 + linePointB.z);
            Point resultPoint2 = new Point(abVector.x * t2 + linePointB.x, abVector.y * t2 + linePointB.y, abVector.z * t2 + linePointB.z);
            result.add(resultPoint1);
            result.add(resultPoint2);
        } else if (discriminant == 0) {
            double t = -bFactor / (2 * aFactor);
            Point resultPoint = new Point(abVector.x * t + linePointB.x, abVector.y * t + linePointB.y, abVector.z * t + linePointB.z);
            result.add(resultPoint);
        }
        return result;
    }

    private static String usage(String message) {
        return message + "\r\n" + usage();
    }

    public static String usage() {
        return "Usage: task2 <file_path>";
    }
}
