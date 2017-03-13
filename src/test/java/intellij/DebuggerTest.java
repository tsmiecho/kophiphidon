package intellij;

import org.junit.Test;

/**
 * @author Tomasz Śmiechowicz
 */
public class DebuggerTest {

    @Test
    public void checkMemoryUsage() throws Exception {
        String name = "Bartek";
        String name2 = new String("Bartek");
        Double value = Double.valueOf(4);
        Double value2 = Double.parseDouble("4.0");
        Double value3 = new Double("4.0");
    }

    @Test
    public void catchSpecificExceptionsWithoutBreakpoints() throws Exception {
        throw new TestException();
    }


    @Test
    public void changeValuesWithoutBlocking() throws Exception {
        String name = "Marcin";
        System.out.println(name + "&");
    }

    @Test
    public void catchOnlyOneThread() throws Exception {
        MutableClass object = new MutableClass("Ala");
        new Thread(new Runnable() {
            @Override
            public void run() {
                object.changeName();
            }
        }).start();
        System.out.println(object.name);
    }
}

class TestException extends Exception {}

class MutableClass {
    String name;

    public MutableClass(String name) {
        this.name = name;
    }

    void changeName(){
        name = "Michał";
    }
}