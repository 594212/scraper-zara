import io.quarkus.runtime.QuarkusApplication;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.UniHelper;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import jdom.XmlUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;


public class ReaderVertx implements QuarkusApplication {

    final Logger LOGGER = LoggerFactory.getLogger(ReaderVertx.class);
    final StopWatch stopWatch = new StopWatch();

    private void watchLog(int i) {
        stopWatch.split();
        LOGGER.info(i + " :: " + stopWatch.toSplitString());
    }


    @ConfigProperty(name = "user.agent", defaultValue = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
    String AGENT;
    private final Vertx vertx;
    private final WebClient webClient;

    @Inject
    public ReaderVertx(Vertx vertx) {
        this.vertx = vertx;
        WebClientOptions options = new WebClientOptions()
                .setUserAgentEnabled(true).setUserAgent(AGENT);

        this.webClient = WebClient.create(vertx, options);

    }


    public Uni<String> readHTML(String _url) {

        return webClient
                .getAbs(_url)
                .putHeader("accept", "*/*")
                .putHeader("accept-encoding", "utf-8")
                .send()
                .onItem().transform(HttpResponse::bodyAsString)
                .onItem().transform(this::scrapeSting);
    }


    private String scrapeSting(String html) {

        Elements html1 = Jsoup.parse(html)
                .select("body > script");

        String string = html1.get(0).html();
        String pattern = "window.zara.viewPayload = ";
        return string.substring(string.indexOf(pattern) + pattern.length(),
                string.length() - 1);
    }

    public Multi<String> readXML(String _url, Function<String, List<String>> function) {

        return webClient.getAbs(_url.replace(".gz", ""))
                .putHeader("accept", "*/*")
                .putHeader("accept-encoding", "utf-8")
                .send()
                .onItem().transform(HttpResponse::bodyAsString)
                .onItem().transform(function)
                .onItem().transformToMulti(rowSet ->
                        Multi.createFrom().iterable(rowSet));
    }


    @Override
    public int run(String... args) {
        String _loc = "src/main/resources/";
        String URL = "https://www.zara.com/sitemaps/sitemap-us-en.xml.gz";


        String loc = _loc + URL.substring(0, URL.length() - 7)
                .replace("https://www.zara.com/sitemaps/sitemap-", "")
                .replaceFirst("-", "/");

        vertx.fileSystem().exists(loc)
                .call(exist -> exist ? Uni.createFrom().nullItem() : vertx.fileSystem().mkdirs(loc))
                .subscribe().with(UniHelper.NOOP);

        readXML(URL, XmlUtil.urlSet)
                .call(htmlSite -> readHTML(htmlSite)
                        .onItem().transform(Buffer::buffer)
                        .call(html -> {
                                    String path = _loc + htmlSite.substring(0, htmlSite.length() - 5).replace("https://www.zara.com", "") + ".json";
                                    return vertx.fileSystem().writeFile(path, html);
                                }
                        )
                        .onFailure().recoverWithNull()

                )
                .subscribe().with(
                        success -> LOGGER.info("THREAD WORKED " + Thread.activeCount() + "\n" + " SUCCESS: " + success),
                        failure -> LOGGER.info("failure: " + failure.toString()));

        return 0;
    }


//    public static void main(String args[]) {
//        String _loc = "src/main/resources/";
//        String htmlSite = "https://www.zara.com/us/en/porcelain-tableware-with-rim-pG45270202100000.html";
//        String path = _loc + htmlSite.substring(0, htmlSite.length() - 5).replace("https://www.zara.com", "") + ".json";
//        System.out.println(path);
//
//        String URL = "https://www.zara.com/sitemaps/sitemap-us-en.xml.gz";
//        String path2 = URL.substring(0, URL.length() - 7)
//                .replace("https://www.zara.com/sitemaps/sitemap-", "")
//                .replaceFirst("-", "/");
//        System.out.println(path2);
//    }

}

