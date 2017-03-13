package fibonacci;

import ratpack.exec.Promise;
import ratpack.http.client.HttpClient;
import ratpack.http.client.ReceivedResponse;
import ratpack.server.RatpackServer;

import java.net.URI;
import java.time.Duration;

/**
 * @author Tomasz Smiechowicz
 */
public class Main {
    public static void main(String... args) throws Exception {
        HttpClient httpClient = HttpClient.of(httpClientSpec -> httpClientSpec.readTimeout(Duration.ofMinutes(2)));
        RatpackServer.start(server -> server
                .serverConfig(serverConfigBuilder -> serverConfigBuilder
                        .port(8080)
                        .threads(4))
                .handlers(chain -> chain
                        .prefix("fast", fib -> fib
                            .get(ctx -> ctx.render("Hello World!"))
                            .get(":n", ctx ->
                                ctx.render(String.valueOf(fibonacci(Integer.valueOf(ctx.getPathTokens().get("n")))))
                            )
                        )
                        .prefix("slow", fib -> fib
                            .get(ctx -> ctx.render("Hello World!"))
                            .get(":n", ctx -> {
                                final Integer n = Integer.valueOf(ctx.getPathTokens().get("n"));
                                if(n < 3){
                                    ctx.render("1");
                                } else {
                                    final Promise<ReceivedResponse> receivedResponsePromiseFibbN_1 = httpClient.get(new URI("http://localhost:8080/slow/" + (n - 1)));
                                    final Promise<Long> promisedFibbN_1 = receivedResponsePromiseFibbN_1.map(bodyResponse -> Long.parseLong(bodyResponse.getBody()
                                                                                                                                                    .getText()));
                                    final Promise<ReceivedResponse> receivedResponsePromiseFibbN_2 = httpClient.get(new URI("http://localhost:8080/slow/" + (n - 2)));
                                    final Promise<Long> promisedFibbN_2 = receivedResponsePromiseFibbN_2.map(bodyResponse -> Long.parseLong(bodyResponse.getBody()
                                                                                                                                                    .getText()));
                                    promisedFibbN_1.then(fibbN_1 -> promisedFibbN_2.then(fibbN_2 -> ctx.render(String.valueOf(fibbN_1 + fibbN_2))));
                                }
                            })
                        )
                )
        );
    }


    static int fibonacci(int n){
        if(n == 0){
            return 0;
        }
        if(n<3){
            return 1;
        }
        return fibonacci(n - 2) + fibonacci(n - 1);
    }
}

