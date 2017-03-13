package springboot.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springboot.http.HttpRequester;

/**
 * @author Tomasz Smiechowicz
 */
@RestController
public class MainController {

    @RequestMapping(value = "/fast/{n:.+}", method = RequestMethod.GET)
    public String fib(@PathVariable("n") int n, Model model) {
        return String.valueOf(fibonacci(n));
    }

    @RequestMapping(value = "/slow/{n:.+}", method = RequestMethod.GET)
    public String slow(@PathVariable("n") int n, Model model) {
        if(n == 0){
            return "0";
        }
        if(n<3){
            return "1";
        }
        final Integer N_2 = Integer.valueOf(HttpRequester.sendGet("http://localhost:8080/slow/" + (n - 2)));
        final Integer N_1 = Integer.valueOf(HttpRequester.sendGet("http://localhost:8080/slow/" + (n - 1)));

        return String.valueOf(N_1 + N_2);
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
