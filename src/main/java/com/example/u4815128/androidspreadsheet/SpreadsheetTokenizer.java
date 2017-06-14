package com.example.u4815128.androidspreadsheet;

/**
 * Tokenizer to work with spreadsheet cell expressions
 *
 * Grammar:
 *
 * letter = "A" | "B" | "C" | "D" | "E" | "F" | "G"
 *        | "H" | "I" | "J" | "K" | "L" | "M" | "N"
 *        | "O" | "P" | "Q" | "R" | "S" | "T" | "U"
 *        | "V" | "W" | "X" | "Y" | "Z" ;
 *
 * nonzero_digit = "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
 * digit = "0" | nonzero_digit ;
 * num = "0" | nonzero_digit, {digit} ;
 *
 * // Cells can be in the range of A1 to Z9
 * var = letter, digit ;
 *
 * symbol = "(" | ")" | "<" | ">"
 *        | "'" | '"' | "=" | "|" | "." | "," | ";" ;
 *
 * operator = "+" | "-" | "*" | "/" ;
 *
 * factor = num | var ;
 * term = factor | factor * term | factor / term ;
 * exp = term | term + exp | term - exp ;
 *
 * exp = num | ["="], "(", num | cell, operator, num | cell, ")" ;
 */
public class SpreadsheetTokenizer extends Tokenizer {

    private String text;
    private int pos;
    private Object current;

    static final char[] whitespace = { ' ', '\n', '\t' };

    SpreadsheetTokenizer(String text) {
        this.text = text;
        pos = 0;
        next();
    }

    @Override
    boolean hasNext() {
        return current != null;
    }

    @Override
    Object current() {
        return current;
    }

    @Override
    void next() {
        consumeWhite();

        char c = 0;
        int textLen = text.length();

        if (pos < textLen)
            c = text.charAt(pos);

        if (pos >= textLen) {
            current = "done";
            return;
        } else if (c == '(') {
            pos++;
            current = "(";
            return;
        } else if (c == ')') {
            pos++;
            current = ")";
            return;
        } else if (c == '+') {
            pos++;
            current = "+";
            return;
        } else if (c == '-') {
            pos++;
            current = "-";
            return;
        } else if (c == '*') {
            pos++;
            current = "*";
            return;
        } else if (c == '/') {
            pos++;
            current = "/";
            return;
        } else if (c == '=') {
            pos++;
            current = "=";
            return;
            // A letter; indicates a 2-digit cell name
        } else if ((int) c >= 65) {
            String cellName = "" + c;
            pos++;
            c = text.charAt(pos);
            cellName += c;
            pos++;
            current = cellName;
            return;
            // A number.
        } else if (Character.isDigit(c)) {
            String number = "" + c;
            pos++;
            if (pos < textLen) {
                while (pos < textLen && Character.isDigit(text.charAt(pos))) {
                    c = text.charAt(pos);
                    number += c;
                    pos++;

                }
            }
            current = Integer.parseInt(number);
            return;
        }
    }

    private void consumeWhite() {
        while (pos < text.length() && isin(text.charAt(pos), whitespace))
            pos++;
    }

    private boolean isin(char c, char charlist[]) {
        for (char nextChar : charlist) {
            if (nextChar == c)
                return true;
        }
        return false;
    }
}
