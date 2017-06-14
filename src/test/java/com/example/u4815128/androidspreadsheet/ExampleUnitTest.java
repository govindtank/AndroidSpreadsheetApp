package com.example.u4815128.androidspreadsheet;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void tokenizerTest() {
        Map<String, Double> mappy = new HashMap<>();
        SpreadsheetTokenizer toke = new SpreadsheetTokenizer("2 + 2 * 7");
        assertTrue(toke.hasNext());
        assertTrue("Actually equals " + toke.current(), toke.current().equals(2));
        toke.next();
        assertTrue("Actually equals " + toke.current(), toke.current().equals("+"));
        toke.next();
        assertTrue("Actually equals " + toke.current(), toke.current().equals(2));
        toke.next();
        assertTrue("Actually equals " + toke.current(), toke.current().equals("*"));
        toke.next();
        assertTrue("Actually equals " + toke.current(), toke.current().equals(7));
    }


    @Test
    public void parserEvaluatorTest() {
        Map<String, Double> mappy = new HashMap<>();
        SpreadsheetTokenizer toke = new SpreadsheetTokenizer("2 + 2 * 7");
        Expression exp = Expression.parseExp(toke);
        assertTrue(exp.firstChild != null);
        assertTrue(exp.secondChild != null);
        assertTrue(exp.evaluate(mappy) == 16);

        SpreadsheetTokenizer toke2 = new SpreadsheetTokenizer("  6 /  3 + 7");
        Expression exp2 = Expression.parseExp(toke2);
        assertTrue(exp2.firstChild != null);
        assertTrue(exp2.secondChild != null);
        assertTrue(exp2.evaluate(mappy) == 9);

    }

}