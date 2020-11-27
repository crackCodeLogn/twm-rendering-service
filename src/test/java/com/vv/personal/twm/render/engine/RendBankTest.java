package com.vv.personal.twm.render.engine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * @author Vivek
 * @since 17/11/20
 */
@RunWith(JUnit4.class)
public class RendBankTest {

    @Test
    public void testGenerateTable() {
        String input = "[{\"name\":\"TR1\",\"bankType\":\"PRIVATE\",\"IFSC\":\"TRN1234\",\"contactNumber\":1213131}, {\"name\":\"TR2\",\"bankType\":\"PRIVATE\",\"IFSC\":\"TRN12344\",\"contactNumber\":12131231}]";
        String result = RendBank.generateTable(input);
        System.out.println(result);
        assertEquals("<table style=\"border: 3px solid black; width:100%\"><tr><th style=\"border:2px solid black; background-color:#cef\">Name</th><th style=\"border:2px solid black; background-color:#cef\">Type</th><th style=\"border:2px solid black; background-color:#cef\">IFSC</th><th style=\"border:2px solid black; background-color:#cef\">Contact</th></tr><tr><td style=\"border:1px solid black;text-align:center\">TR1</td><td style=\"border:1px solid black;text-align:center\">PRIVATE</td><td style=\"border:1px solid black;text-align:center\">TRN1234</td><td style=\"border:1px solid black;text-align:center\">1213131</td></tr><tr><td style=\"border:1px solid black;text-align:center\">TR2</td><td style=\"border:1px solid black;text-align:center\">PRIVATE</td><td style=\"border:1px solid black;text-align:center\">TRN12344</td><td style=\"border:1px solid black;text-align:center\">12131231</td></tr></table>", result);
    }

}