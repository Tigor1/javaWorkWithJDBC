import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[a-z&&[def]]");
        Matcher matcher = pattern.matcher("alpd");
        System.out.println(matcher.matches());
    }
}
