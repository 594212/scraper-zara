package data;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import org.bson.Document;

import javax.inject.Inject;
import java.util.List;

public class XmlClient {

    @Inject
    ReactiveMongoClient mongoClient;

    public Uni<List<Sitemap>> sitemaps() {

        return getCollection().find().map(
                document -> {
                    Sitemap sitemap = new Sitemap();
                    sitemap.setUrl(document.getString("url"));
                    sitemap.setUpdatedAt(document.getString("created_at"));
                    return sitemap;
                }
        ).collect().asList();
    }

    public Uni<Void> add(Sitemap sitemap) {
        Document document = new Document()
                .append("url", sitemap.getUrl())
                .append("created_at", sitemap.getUpdatedAt());
        return getCollection().insertOne(document)
                .onItem().ignore().andContinueWithNull();
    }

    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("zaradb").getCollection("sitemaps");
    }

}
