package com.example.u4815128.androidspreadsheet;

import java.util.Map;

/**
 * Created by u4815128 on 28/04/17.
 */
public class MultiplyExpression extends Expression {

    MultiplyExpression(Expression first, Expression second) {
        this.firstChild = first;
        this.secondChild = second;
    }

    @Override
    double evaluate(Map<String, Double> mappy) {
        return firstChild.evaluate(mappy) * secondChild.evaluate(mappy);
    }

    @Override
    String getSymbol() {
        return "x";
    }

}
