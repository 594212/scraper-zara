import io.quarkus.runtime.QuarkusApplication;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xml.MapUrl;
import xml.Sitemap;
import xml.XmlUtil;


import javax.inject.Inject;


public class ReaderVertx implements QuarkusApplication {

    Logger LOGGER = LoggerFactory.getLogger(ReaderVertx.class);

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


    public Uni<Uni<Void>> readHTML(String _url, String _loc) {
        String loc = _loc + FilenameUtils.getBaseName(_url) + ".json";

        return webClient
                .getAbs(_url)
                .putHeader("accept", "*/*")
                .putHeader("accept-encoding", "utf-8")
                .send()
                .onItem().transform(HttpResponse::bodyAsString)
                .onItem().transform(this::scrapeSting)
                .onItem().transform(Buffer::buffer)
                .onItem().transform(html -> vertx.fileSystem().writeFile(loc, html));
    }


    private String scrapeSting(String html) {
        return Jsoup.parse(html)
                .select("script[type$=text/javascript]")
                .get(5).html();
    }

    public Multi<String> readXML(String _url) {
        return webClient.getAbs(_url.replace(".gz", ""))
                .putHeader("accept", "*/*")
                .putHeader("accept-encoding", "utf-8")
                .send()
                .onItem().transform(HttpResponse::bodyAsString)
                .onItem().transform(XmlUtil::stringToSitemap)
                .onItem().transformToMulti(rowSet ->
                        Multi.createFrom().iterable(rowSet))
                .onItem().transform(Sitemap::getUrl)
                .onItem().transformToMultiAndMerge(mapUrl ->
                        webClient.getAbs(mapUrl.replace(".gz", ""))
                                .putHeader("accept", "*/*")
                                .putHeader("accept-encoding", "utf-8")
                                .send()
                                .onItem().transform(HttpResponse::bodyAsString)
                                .onItem().transform(XmlUtil::stringToMapUrl)
                                .onItem().transformToMulti(rowSet ->
                                        Multi.createFrom().iterable(rowSet))
                                .onItem().transform(MapUrl::getUrl)
                )
                .onItem().invoke( URL -> LOGGER.info("URL: " + URL));
    }


    @Override
    public int run(String... args) {
        String loc = "src/main/resources/";
        String URL = "https://www.zara.com/sitemaps/sitemap-index.xml.gz";
        readXML(URL).onItem()
                .call(htmlSite -> readHTML(htmlSite, loc))
                .subscribe().with(success -> LOGGER.info("success: " + success),
                        failure -> LOGGER.info("failure: " + failure.toString()));
        return 0;
    }
}

