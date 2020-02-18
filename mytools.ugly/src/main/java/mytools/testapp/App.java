package mytools.testapp;

import mytools.stringparser.StringParsers;
import mytools.util.date.Dates;

public final class App {

    private App() { }

    public static void main(String[] args) {
        System.out.println(Dates.DATE_TO_STRING_FORMAT);
        System.out.println(StringParsers.Defaults.STRING_PARSER
                .parse(new java.util.Date().toString()));
    }

}
