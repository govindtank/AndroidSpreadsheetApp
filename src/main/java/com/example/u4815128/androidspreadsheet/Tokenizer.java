package com.example.u4815128.androidspreadsheet;

import java.util.Objects;

/**
 * Tokenizer for my Android studio spreadsheet app
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
 * num = "0" | ["-"], nonzero_digit, {digit} ;
 *
 * // Cells can be in the range of A1 to Z9
 * cell = letter, digit ;
 *
 * symbol = "(" | ")" | "<" | ">"
 *        | "'" | '"' | "=" | "|" | "." | "," | ";" ;
 *
 * operator = "+" | "-" | "*" | "/" ;
 *
 * exp = num | cell, operator, num | cell;
 *
 * assignment = "=", cell | exp ;
 */
public abstract class Tokenizer {

    abstract boolean hasNext();

    abstract Object current();

    abstract void next();

    public void parse(Object o) {
        if (current() == null || !current().equals(o))
            throw new Error();

        next();
    }
}

