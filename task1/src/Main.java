import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    final static String maskOfPositional = "0123456789abcdefghijklmnopqrstuvwxyz";
    final static Set<String> romanNames = new HashSet<>(Arrays.asList("rome", "roman"));
    final static Set<String> catNames = new HashSet<>(Arrays.asList("котики", "cats", "cat"));

    public static String itoBase(int nb, String base) {
        if (nb < 0) {
            return usage("Введенное число должно быть неотрицательным");
        }
        String result;
        if (maskOfPositional.startsWith(base) && base.length() > 1) {
            result = decimalToPositional(nb, base.length());
        } else if (romanNames.contains(base.toLowerCase())) {
            result = decimalToRoman(nb);
        } else if (catNames.contains(base.toLowerCase())) {
            result = decimalToCats(nb);
        } else {
            result = usage("Неизвестная система счисления: " + base);
        }
        return result;
    }

    public static String itoBase(String nb, String baseSrc, String baseDst) {
        int n;
        if (maskOfPositional.startsWith(baseSrc) && baseSrc.length() > 1) {
            try {
                n = positionalToDecimal(nb, baseSrc.length());
            } catch (NumberFormatException e) {
                return usage("Некорректное число для " + baseSrc.length() + "-ичной системы счисления: " + nb);
            }
        } else if (romanNames.contains(baseSrc.toLowerCase())) {
            try {
                n = romanToDecimal(nb);
            } catch (RomanNumberFormatException e) {
                return usage("Некорректное значение числа для римской системы счисления: " + nb);
            }
        } else if (catNames.contains(baseSrc.toLowerCase())) {
            try {
                n = catsToDecimal(nb);
            } catch (CatFormatException e) {
                return usage("Некорректная строка для системы счисления \"котики\" : " + nb);
            }
        } else {
            return usage("Неизвестная система счисления: " + baseSrc);
        }
        return itoBase(n, baseDst);
    }

    private static String decimalToPositional(int nb, int radix) {
        return new BigInteger(String.valueOf(nb), 10).toString(radix);
    }

    private static int positionalToDecimal(String nb, int radix) throws NumberFormatException {
        return Integer.parseInt(new BigInteger(nb, radix).toString(10));
    }

    private static String decimalToRoman(int nb) {
        if (nb == 0 || nb > 3999) {
            return usage("Конвертация в римскую систему счисления возможна для чисел от 1 до 3999 включительно");
        }
        String[] romanChars = {"M", "CM", "D", "C", "XC", "L", "X", "IX", "V", "I"};
        int[] romanValues = {1000, 900, 500, 100, 90, 50, 10, 9, 5, 1};
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < romanValues.length; i++) {
            int number = nb / romanValues[i];
            if (number == 0) continue;
            result.append(number == 4 ? romanChars[i] + romanChars[i - 1] :
                    new String(new char[number]).replace("\0", romanChars[i]));
            nb = nb % romanValues[i];
        }
        return result.toString();
    }

    private static int romanToDecimal(String romeNumber) throws RomanNumberFormatException {
        if (!romeNumber.matches("[IVXLCDM]+")) {
            throw new RomanNumberFormatException();
        }

        Map<Character, Integer> romanChars = new HashMap<>();
        romanChars.put('I', 1);
        romanChars.put('V', 5);
        romanChars.put('X', 10);
        romanChars.put('L', 50);
        romanChars.put('C', 100);
        romanChars.put('D', 500);
        romanChars.put('M', 1000);
        int res = 0;
        for (int i = 0; i < romeNumber.length(); i += 1) {
            if (i == 0 || romanChars.get(romeNumber.charAt(i)) <= romanChars.get(romeNumber.charAt(i - 1)))
                res += romanChars.get(romeNumber.charAt(i));
            else
                res += romanChars.get(romeNumber.charAt(i)) - 2 * romanChars.get(romeNumber.charAt(i - 1));
        }
        return res;
    }

    private static String decimalToCats(int nb) {
        return String.join("  ", Collections.nCopies(nb, "=^..^="));
    }

    private static int catsToDecimal(String nb) throws CatFormatException {
        Pattern oneCat = Pattern.compile("=\\^\\.\\.\\^=");
        if (!nb.matches("^( *" + oneCat + " *)+$")) {
            throw new CatFormatException();
        }
        Matcher matcher = oneCat.matcher(nb);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static String usage() {
        return "Usage:\r\n" +
                "   task1 <number> <num_sys>                      - to convert <number> in decimal to other <num_sys>\r\n" +
                "   task1 <number> <src_num_sys> <dest_num_sys>   - to convert <number> in <src_num_sys> to <dest_num_sys>\r\n" +
                "\r\n" +
                "Available numeral systems:\r\n" +
                "   - positional (\"01\" - for base 2, \"012\" - for base 3, \"0123456789abcdef\" - for base 16, etc.)\r\n" +
                "   - roman, rome\r\n" +
                "   - cat, cats";
    }

    private static String usage(String message) {
        return message + "\r\n" + usage();
    }

    public static void main(String[] args) {
        String result;
        if (args.length == 2) {
            try {
                result = itoBase(Integer.parseInt(args[0]), args[1]);
            } catch (NumberFormatException e) {
                result = usage("Неверный формат числа для десятичной системы счисления: " + args[0]);
            }
        } else if (args.length == 3) {
            result = itoBase(args[0], args[1], args[2]);
        } else {
            result = usage();
        }
        System.out.println(result);
    }
}
