import io.vertx.mutiny.ext.web.client.WebClient;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CamelRouter extends RouteBuilder {
    Bot bot = new Bot();


    @ConfigProperty(name = "bot.token")
    String TOKEN;

    @Inject
    WebClient webClient;

    @Override
    public void configure() throws Exception {
        from("telegram:bots?authorizationToken="+TOKEN)
                .bean(bot)
                .to("telegram:bots?authorizationToken="+TOKEN)
                .to("log:INFO?showHeaders=true");
    }


}


class Test {
    public static void main(String args[]) throws Exception {

        CamelContext context = new DefaultCamelContext();
        context.start();

        context.addRoutes(new CamelRouter());
    }

}
