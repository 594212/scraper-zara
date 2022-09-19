import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.vertx.mutiny.core.Vertx;

import javax.inject.Inject;
@QuarkusMain
public class Main {
    public static void main(String args[]) {
        String loc = "src/main/resources/";
        String URL = "https://www.zara.com/us/en/narciso-rodriguez-oversized-jumpsuit-p08132811.html";
//        Files.createDirectories(Paths.get("/Your/Path/Here"));


//        old.Reader.readAndWriteHTMLtoJSON(URL, loc + "zara");
        Quarkus.run(ReaderVertx.class, args);


    }
}
