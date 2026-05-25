import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppTest {

    @Test
    public void testGetGreeting() {
        String expected = "Hello, AI Automation Project!";
        String actual = App.getGreeting();
        assertEquals(expected, actual);
    }
}