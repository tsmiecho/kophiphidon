package nonblocking;

import nonblocking.http.HttpRequester;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Tomasz Śmiechowicz
 */
public class AsyncJavaTest {

    @Test
    public void synchronously() throws Exception {
        final String response = HttpRequester.sendGet("http://superlot.pl/");
        System.out.println(response);
    }

    @Test
    public void oldAsyncApproach() throws Exception {
        ExecutorService es = Executors.newSingleThreadExecutor();

        final Future<String> futureResponse = es.submit(new Callable<String>() {
            public String call() throws Exception {
                return HttpRequester.sendGet("http://superlot.pl/");
            }
        });
        System.out.println(futureResponse.get());
    }

    @Test
    public void atgScheduler() throws Exception {
        final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(2);
        threadPool.scheduleAtFixedRate(() -> System.out.println(Thread.currentThread()), 0, 1, TimeUnit.SECONDS);
        Thread.sleep(10000);
    }

    @Test
    public void newApproach() throws Exception {
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            //System.out.println(Thread.currentThread());
            return HttpRequester.sendGet("http://superlot.pl");
        });
        System.out.println(future.get());
    }

    @Test
    public void firstCallback() throws Exception {
        final CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> HttpRequester.sendGet("http://superlot.pl"));
        final CompletableFuture<Character> thenApply = future2.thenApply(string -> string.charAt(4));
        System.out.println(thenApply.get());
    }

    @Test
    public void nestedCallbacks() throws Exception {
        final CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> HttpRequester.sendGet("http://superlot.pl"))
                .thenApply(string -> string.substring(10, 20))
                .thenApply(String::hashCode);
        System.out.println(future.get());
    }

    @Test
    public void asyncTaskInsideAnotherAsyncTask() throws Exception {

        final CompletableFuture<CompletableFuture<String>> future = CompletableFuture.supplyAsync(() -> HttpRequester.sendGet("http://superlot.pl"))
                .thenApply(string -> string.substring(10, 20))
                .thenApply(i -> CompletableFuture.supplyAsync(() -> i));

        final CompletableFuture<String> compose = CompletableFuture.supplyAsync(() -> HttpRequester.sendGet("http://superlot.pl"))
                .thenApply(string -> string.substring(10, 20))
                .thenCompose(i -> CompletableFuture.supplyAsync(() -> i));

        System.out.println(compose);
    }
}
