package com.example.u4815128.androidspreadsheet;

import java.util.Map;

/**
 * A class to hold a literal value
 */
public class LiteralExpression extends Expression {

    double value;

    LiteralExpression(double value) {
        this.value = value;
    }

    @Override
    double evaluate(Map<String, Double> mappy) {
        return value;
    }

    @Override
    String getSymbol() {
        return Double.toString(value);
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
