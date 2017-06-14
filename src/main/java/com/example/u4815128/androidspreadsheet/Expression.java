package com.example.u4815128.androidspreadsheet;

import java.util.*;

/**
 * An abstract class to be extended by concrete Expressions
 */
abstract class Expression {

    Expression firstChild = null;
    Expression secondChild = null;
    Map<String, Double> mappy = new HashMap<>();

    static LiteralExpression literal(double value) {
        return new LiteralExpression(value);
    }

    static AddExpression add(Expression first, Expression second) {
        return new AddExpression(first, second);
    }

    static DivideExpression divide(Expression first, Expression second) {
        return new DivideExpression(first, second);
    }

    static MultiplyExpression multiply(Expression first, Expression second) {
        return new MultiplyExpression(first, second);
    }

    static SubtractExpression subtract(Expression first, Expression second) {
        return new SubtractExpression(first, second);
    }

    static VariableExpression variable(String key) {
        return new VariableExpression(key);
    }

    // Method to parseExp tokens
    static Expression parseExp(SpreadsheetTokenizer toke) {
        Expression term = parseTerm(toke);
        // Found a new number literal
        if (toke.current().equals("+")) {
            toke.next();
            Expression expr = parseExp(toke);
            return new AddExpression(term, expr);
        } else if (toke.current().equals("-")) {
            toke.next();
            Expression expr = parseExp(toke);
            return new SubtractExpression(term, expr);
        } else {
            return term;
        }
    }

    static Expression parseTerm(SpreadsheetTokenizer toke) {
        Expression factor = parseFactor(toke);
        if (toke.current().equals("*")) {
            toke.next();
            Expression term = parseTerm(toke);
            return new MultiplyExpression(factor, term);
        } else if (toke.current().equals("/")) {
            toke.next();
            Expression term = parseTerm(toke);
            return new DivideExpression(factor, term);
        } else {
            return factor;
        }
    }

    static Expression parseFactor(SpreadsheetTokenizer toke) {
        if (toke.current() instanceof Integer) {
            Expression expr = new LiteralExpression((double) ((Integer) toke.current()).intValue());
            toke.next();
            return expr;
        } else {
            Expression expr = new VariableExpression((String) toke.current());
            toke.next();
            return expr;
        }
    }

    /**
     * Methods to be implemented by concrete classes
     */
    // Return value of an expression node, whether a computed value or a literal
    abstract double evaluate(Map<String, Double> mappy);

    abstract String getSymbol();

    int size() {
        return firstChild.size() + secondChild.size() + 1;
    }

    int height() {
        return Math.max((firstChild.height() + 1), secondChild.height() + 1);
    }

    int operators() {
        return firstChild.operators() + secondChild.operators() + 1;
    }

    Expression simplify() {
        if (firstChild instanceof VariableExpression || secondChild instanceof VariableExpression) {
            return this;
        } else
            return literal(this.evaluate(mappy));
    }

    void printTree() {
        // Array of arrays that will store symbols that need to be printed for each level of the tree
        ArrayList<String> levelStrings = new ArrayList<>();
        // Create a String for level 0 (tree root) and add this Element's symbol
        String tempString = getSymbol();
        levelStrings.add(tempString);

        if (firstChild != null)
            firstChild.buildTree(0, 0, getSymbol().length() - 1, false, levelStrings);
        if (secondChild != null) {
            tempString = levelStrings.get(0);
            secondChild.buildTree(0, tempString.length() - getSymbol().length(), tempString.length(), true, levelStrings);
        }

        // Pad all Strings to the same length to make next section easier
        int longestStringLength = 0;
        for (String currentString : levelStrings) {
            if (currentString.length() > longestStringLength)
                longestStringLength = currentString.length();
        }
        // Pad as appropriate
        for (int i = 0; i < levelStrings.size(); i++) {
            String currentString = levelStrings.get(i);
            int spacesToAdd = longestStringLength - currentString.length();
            for (int j = 0; j < spacesToAdd; j++) {
                currentString = currentString + " ";
            }
            levelStrings.set(i, currentString);
        }

        // Code to add joining lines between elements in the tree. Scroll through pairs of adjacent layers looking for
        // parents and children to link
        for (int i = 0; i < levelStrings.size() - 1; i++) {
            String parentLevel = levelStrings.get(i);
            String childLevel = levelStrings.get(i + 1);
            String topLine = "";
            String bottomLine = "";
            // Will build two lines at once. Booleans to track where parents and children are
            boolean connectingChildren = false;
            boolean traversingChild = false;
            // Iterate through the parent and child levels simultaneous checking for features
            for (int j = 0; j < parentLevel.length(); j++) {
                // First consider building the bottom line
                // Reading a child node
                if (childLevel.charAt(j) != ' ') {
                    // Found a new child, draw a vertical line
                    if (!traversingChild) {
                        traversingChild = true;
                        bottomLine = bottomLine + "|";
                        // If currently connecting, stop; otherwise start
                        connectingChildren = !connectingChildren;
                        // Was already in the child
                    } else {
                        bottomLine = bottomLine + " ";
                    }
                    // Not in a child
                } else {
                    bottomLine = bottomLine + " ";
                    traversingChild = false;
                }
                // Now build top line
                // Found a parent - draw a horizontal line
                if (parentLevel.charAt(j) == 'x' || parentLevel.charAt(j) == '+' ||
                        parentLevel.charAt(j) == '-' || parentLevel.charAt(j) == '%') {
                    topLine = topLine + "|";
                    // draw line connecting two children
                } else if (connectingChildren) {
                    topLine = topLine + "_";
                } else {
                    // May have just finished connecting two children, in which case need one more underscore
                    if (bottomLine.charAt(j) == '|') {
                        topLine = topLine + "_";
                    } else {
                        topLine = topLine + " ";
                    }
                }
            }
            System.out.println(parentLevel);
            System.out.println(topLine);
            System.out.println(bottomLine);
        }
        System.out.println(levelStrings.get(levelStrings.size() - 1) + "\n");
    }

    private void buildTree(int parentLevel, int parentStart, int parentStop, boolean parentLeft, List<String> treeStrings) {

        // Check if this the first element discovered at a given level and add empty string if so
        if (parentLevel + 1 == treeStrings.size())
            treeStrings.add("");

        // Get string and modify by adding current symbol
        String tempString = treeStrings.get(parentLevel + 1);
        // Add a space if something already on level so not adjacent
        if (tempString.length() > 0)
            tempString = tempString + " ";

        int currentX = tempString.length();

        // Parent should be to the left of current symbol
        if (parentLeft) {
            // If needed, add extra spaces to current level String before adding this node's symbol
            if (parentStop >= currentX) {
                int spacesToAdd = parentStop - currentX + 1;
                for (int i = 0; i < spacesToAdd; i++) {
                    tempString = tempString + " ";
                }
            }
            tempString = tempString + getSymbol();
        // parent should be to the Right
        } else {
            // Add spaces before parent if needed (must also add spaces to all higher levels)
            if (parentStart <= currentX + getSymbol().length()) {
                int spacesToAdd = (currentX + getSymbol().length()) - parentStart + 1;
                // Must add spaces to levels higher than the current
                for (int i = 0; i <= parentLevel; i++) {
                    String updatingString = treeStrings.get(i);
                    if (updatingString.length() >= parentStart) {
                        String firstHalf = updatingString.substring(0, parentStart);
                        String secondHalf = updatingString.substring(parentStart, updatingString.length());
                        String toInsert = "";
                        for (int j = 0; j < spacesToAdd; j++) {
                            toInsert = toInsert + " ";
                        }
                        updatingString = firstHalf + toInsert + secondHalf;
                        treeStrings.set(i, updatingString);
                    }
                }
            }
            // Add spaces before child if needed
            if (parentStart - (currentX + getSymbol().length()) > 2) {
                int spacesToAdd = parentStart - (currentX + getSymbol().length()) - 1;
                for (int i = 0; i < spacesToAdd; i++) {
                    tempString = " " + tempString;
                }
            }
            tempString = tempString + getSymbol();
        }

        // Finally, update string in the Array
        treeStrings.set(parentLevel + 1, tempString);

        // Recurse into children if they exist
        if (firstChild != null)
            firstChild.buildTree(parentLevel + 1, tempString.length() - getSymbol().length(), tempString.length(), false, treeStrings);
        if (secondChild != null) {
            tempString = treeStrings.get(parentLevel + 1);
            secondChild.buildTree(parentLevel + 1, tempString.length() - getSymbol().length(), tempString.length(), true, treeStrings);
        }

    }

}
