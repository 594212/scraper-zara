package old;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlArticle;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import xml.ListSitemap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class Reader {


    public static void readAndWriteHTMLtoJSON(String url, String loc) {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);

        try {
            HtmlPage page = webClient.getPage(url);
            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            webClient.close();
            HtmlArticle article = page.getFirstByXPath("//article");
            HtmlScript script = article.getFirstByXPath("//script[@type='application/ld+json']");
            String context = script.getTextContent();


            FileWriter zaraFile = new FileWriter(loc + ".json");
            zaraFile.write(context);
            zaraFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static ListSitemap readXML(String _uri) throws JAXBException, IOException, ParserConfigurationException, SAXException {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        UnexpectedPage xml = webClient.getPage(_uri.replace(".gz", ""));
        webClient.close();

        JAXBContext context = JAXBContext.newInstance(ListSitemap.class);
        Unmarshaller um = context.createUnmarshaller();

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(false);
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

        Document document = documentBuilder.parse(xml.getInputStream());
        ListSitemap sitemaps = (ListSitemap) um.unmarshal(document);
        return sitemaps;
    }


    public static void main(String args[]) {
        String url = "https://www.zara.com/sitemaps/sitemap-index.xml.gz";
        String loc = "src/main/resources/";
        String URL = "https://www.zara.com/us/en/narciso-rodriguez-oversized-jumpsuit-p08132811.html";
        try {
            ;
            readXML(url);
        } catch (JAXBException | IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
