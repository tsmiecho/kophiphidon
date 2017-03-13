package fibonacci;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Tomasz Smiechowicz
 */
public class MainTest {

    Main main = new Main();

    @Test
    public void testFibonacci() throws Exception {

        Assert.assertEquals(0, main.fibonacci(0));
        Assert.assertEquals(1, main.fibonacci(1));
        Assert.assertEquals(1, main.fibonacci(2));
        Assert.assertEquals(2, main.fibonacci(3));
        Assert.assertEquals(3, main.fibonacci(4));
        Assert.assertEquals(5, main.fibonacci(5));
        Assert.assertEquals(8, main.fibonacci(6));
        Assert.assertEquals(55, main.fibonacci(10));
        Assert.assertEquals(2584, main.fibonacci(18));
    }
}