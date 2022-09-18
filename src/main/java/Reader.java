import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlArticle;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import org.eclipse.jetty.util.resource.Resource;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

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


            FileWriter zaraFile = new FileWriter(loc+".json");
            zaraFile.write(context);
            zaraFile.close();
        } catch (IOException ex) {
            System.out.println("An error occurred " + ex);
        }

    }

    public void downloadAndParseXML(String _url) {

        try {
            URL url = new URL(_url);
            InputStream stream = url.openStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
