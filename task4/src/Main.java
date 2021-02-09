import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(usage());
        } else {
            String word = args[0];
            String mask = args[1];
            System.out.println(checkWords(word, mask));
        }
    }

    public static String checkWords(String word, String mask) {
        if (word.equals(mask)) {
            return "OK";
        }
        if (!mask.contains("*")) {
            return "KO";
        }

        Pattern specialRegexChars = Pattern.compile("[{}()\\[\\].+?^$\\\\|]");
        String s4 = specialRegexChars.matcher(mask).replaceAll("\\\\$0").replace("*", ".*");
        if (word.matches(s4)) {
            return "OK";
        } else {
            return "KO";
        }
    }

    public static String usage() {
        return "Usage: task4 <first_word> <second_word>";
    }
}
