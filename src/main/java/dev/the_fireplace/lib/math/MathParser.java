package dev.the_fireplace.lib.math;

/**
 * Formula parse code taken and modified from https://stackoverflow.com/a/26227947
 * Use this instead of javascript to avoid any problems with Minecraft's built in Java version not having a JavaScript engine.
 */
final class MathParser {
    private int currentPosition = -1;
    private int currentCharacter;
    private final String formula;

    MathParser(String formula) {
        this.formula = formula;
    }

    private void nextChar() {
        currentCharacter = (++currentPosition < formula.length())
                ? formula.charAt(currentPosition)
                : -1;
    }

    private boolean eat(int charToEat) {
        while (currentCharacter == ' ') {
            nextChar();
        }
        if (currentCharacter == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    double parse() {
        nextChar();
        double x = parseExpression();
        if (currentPosition < formula.length()) {
            throw new RuntimeException("Unexpected: " + (char) currentCharacter);
        }
        return x;
    }

    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor
    // factor = `+` factor | `-` factor | `(` expression `)`
    //        | number | functionName factor | factor `^` factor
    private double parseExpression() {
        double x = parseTerm();
        for (; ; ) {
            if (eat('+')) {
                x += parseTerm(); // addition
            } else if (eat('-')) {
                x -= parseTerm(); // subtraction
            } else {
                return x;
            }
        }
    }

    private double parseTerm() {
        double x = parseFactor();
        for (; ; ) {
            if (eat('*')) {
                x *= parseFactor(); // multiplication
            } else if (eat('/')) {
                x /= parseFactor(); // division
            } else {
                return x;
            }
        }
    }

    private double parseFactor() {
        if (eat('+')) {
            return parseFactor(); // unary plus
        }
        if (eat('-')) {
            return -parseFactor(); // unary minus
        }

        double x;
        int startPos = this.currentPosition;
        if (eat('(')) { // parentheses
            x = parseExpression();
            eat(')');
        } else if (isNumberCharacter()) { // numbers
            navigatePastNumber();
            x = Double.parseDouble(formula.substring(startPos, this.currentPosition));
        } else if (isLetter()) { // functions
            navigatePastFunctionName();
            String func = formula.substring(startPos, this.currentPosition);
            x = parseFactor();
            x = performFunction(x, func);
        } else {
            throw new RuntimeException("Unexpected: " + (char) currentCharacter);
        }

        if (eat('^')) {
            x = Math.pow(x, parseFactor()); // exponentiation
        }

        return x;
    }

    private boolean isNumberCharacter() {
        return isNumeric() || currentCharacter == '.';
    }

    private boolean isNumeric() {
        return currentCharacter >= '0' && currentCharacter <= '9';
    }

    private boolean isLetter() {
        return currentCharacter >= 'a' && currentCharacter <= 'z';
    }

    private void navigatePastNumber() {
        while (isNumberCharacter()) {
            nextChar();
        }
    }

    private void navigatePastFunctionName() {
        while (isLetter()) {
            nextChar();
        }
    }

    private double performFunction(double x, String func) {
        switch (func) {
            //TODO Make it possible to use these
            case "sqrt" -> x = Math.sqrt(x);
            case "sin" -> x = Math.sin(Math.toRadians(x));
            case "cos" -> x = Math.cos(Math.toRadians(x));
            case "tan" -> x = Math.tan(Math.toRadians(x));
            default -> throw new RuntimeException("Unknown function: " + func);
        }
        return x;
    }
}
