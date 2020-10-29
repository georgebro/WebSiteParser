import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GGB
 * on 27-10-2020.
 * Class: Parser.java
 */

public class Parser {

    // pattern for regular expression -> \d{2}\.\d{2} <-(29.10 our date)
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");


    // Document return Object_array of HTML items by hierarchy
    private static Document getPage() throws IOException {
        String MYURL = "http://www.pogoda.spb.ru/english/";
        Document myPage = Jsoup.parse(new URL(MYURL), 3000);
        return myPage;
    }


    // regular expression -> getting date from the HTML code
    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate); //
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("CAN'T EXTRACT DATE FROM THE STRING!");
    }


    //function that printing our data from HTML site
    private static int printValues(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            Element valueLn = values.get(3);
            boolean isMorning = valueLn.text().contains(" MORNING! ");
            if (isMorning) {
                iterationCount = 3;
            }
        }
            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "   ");
                }
                System.out.println();
            }
        System.out.println();
        return iterationCount;
        }


        public static void main ( String[]args) throws Exception {
            Document page = getPage();
            Element tableWth = page.select("table[class=wt]").first();
            Elements names = tableWth.select("tr[class=wth]");
            Elements values = tableWth.select("tr[valign=top]");
            for (Element name : names) {
                String dateString = name.select("th[id=dt]").text();
                String date = getDateFromString(dateString);
                System.out.println(date+
                        " <WEATHER CONDITIONS>  <TEMPERATURE C°>  <PRESSURE>  <Rel.HUMIDITY>  <WIND> ");
                int index = 0;
                printValues(values, index);
            }
        }
    }
