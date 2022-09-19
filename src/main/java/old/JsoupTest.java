package old;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class JsoupTest {
    public static void main(String args[]) throws IOException {
        String url = "src/main/resources/NARCISO RODRIGUEZ OVERSIZED JUMPSUIT - Oyster White _ ZARA United States.html";
        Document parse = Jsoup.parse(new File(url),"UTF-8");
//        .*window\\.zara\\.viewPayload.*
        Elements elements = parse.select("body > script:nth-child(4)");
//                .stream()
//                .flatMap(el -> el.getElementsByAttributeValue("type", "text/javascript").stream())
//                .flatMap(el -> el.getElementsMatchingText("window.zara.viewPayload").stream())
//                .findFirst();


        FileWriter fileWriter = new FileWriter("src/main/resources/test.json", false);
        fileWriter.write(elements.html());
//        if (elements.isPresent()) fileWriter.write(elements.get().html());
        fileWriter.close();


    }
}
