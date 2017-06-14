package com.example.u4815128.androidspreadsheet;

import java.util.Map;

/**
 * Class for variables, which can be any kind of expression
 */
public class VariableExpression extends Expression {

    private String key;

    VariableExpression(String key) {
        this.key = key;
    }

    @Override
    double evaluate(Map<String, Double> mappy) {
        System.out.println("In HashMap var eval");
        return mappy.get(key);
    }

    @Override
    String getSymbol() {
        return key;
    }

    @Override
    int size() {
        return 1;
    }

    @Override
    int height() {
        return 1;
    }

    @Override
    int operators() {
        return 0;
    }
}
