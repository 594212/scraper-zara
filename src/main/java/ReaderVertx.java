import io.quarkus.runtime.QuarkusApplication;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import jdom.XmlUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;



public class ReaderVertx implements QuarkusApplication {

    final Logger LOGGER = LoggerFactory.getLogger(ReaderVertx.class);
    final StopWatch stopWatch = new StopWatch();
    private void watchLog(int i) {
        stopWatch.split();
        LOGGER.info(i+" :: " + stopWatch.toSplitString());
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

    public Uni<Buffer> readHTML(String _url, String _loc) {
        String loc = _loc + FilenameUtils.getBaseName(_url) + ".json";

        return webClient
                .getAbs(_url)
                .putHeader("accept", "*/*")
                .putHeader("accept-encoding", "utf-8")
                .send()
                .onItem().transform(HttpResponse::bodyAsString)
                .onItem().transform(this::scrapeSting)
                .onItem().transform(Buffer::buffer)
                .call(html -> vertx.fileSystem().writeFile(loc, html))
                .onFailure().recoverWithNull();
    }


    private String scrapeSting(String html) {

        Elements html1 = Jsoup.parse(html)
                .select("body > script");
        String string = html1.get(0).html().replaceFirst("window.zara.appConfig = ", "");
        return string.substring(0, string.length() - 1);
    }

    public Multi<String> readXMLtoMap(String _url) {

        return webClient.getAbs(_url.replace(".gz", ""))
                .putHeader("accept", "*/*")
                .putHeader("accept-encoding", "utf-8")
                .send()
                .onItem().transform(HttpResponse::bodyAsString)
                .onItem().transform(XmlUtil.sitemaps)
                .onItem().transformToMulti(rowSet ->
                        Multi.createFrom().iterable(rowSet));
    }



    public Multi<String> readXMLtoURL(String url) {
        return webClient.getAbs(url.replace(".gz", ""))
                .putHeader("accept", "*/*")
                .putHeader("accept-encoding", "utf-8")
                .send()
                .onItem().transform(HttpResponse::bodyAsString)
                .onItem().transform(XmlUtil.urlSet)
                .onItem().transformToMulti(rowSet ->
                        Multi.createFrom().iterable(rowSet));
    }


    @Override
    public int run(String... args) {
        String loc = "src/main/resources/";
        String URL = "https://www.zara.com/sitemaps/sitemap-ru-ru.xml.gz";
        readXMLtoURL(URL).onItem()
                .call(htmlSite -> readHTML(htmlSite, loc))
                .subscribe().with(
                        success -> LOGGER.info("THREAD WORKED " + Thread.activeCount() + "\n" + " SUCCESS: " + success),
                        failure -> LOGGER.info("failure: " + failure.toString()));

        return 0;
    }
}

