package edu.wisc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class PrettyPrinterTest {
    private final PrettyPrinter printer = new PrettyPrinter();

    @Test
    void printsIntegerAndBooleanLiterals() {
        assertAll(
            () -> assertEquals("42", printer.print(new Expr.Literal(42))),
            () -> assertEquals("true", printer.print(new Expr.Literal(true))),
            () -> assertEquals("false", printer.print(new Expr.Literal(false)))
        );
    }

    @Test
    void printsVariables() {
        assertEquals("counter", printer.print(new Expr.Variable("counter")));
    }

    @Test
    void printsEveryBinaryOperator() {
        Expr left = new Expr.Variable("left");
        Expr right = new Expr.Variable("right");

        assertAll(
            () -> assertEquals("left + right", printBinary(left, Operator.PLUS, right)),
            () -> assertEquals("left - right", printBinary(left, Operator.MINUS, right)),
            () -> assertEquals("left * right", printBinary(left, Operator.MULTIPLY, right)),
            () -> assertEquals("left / right", printBinary(left, Operator.DIVIDE, right)),
            () -> assertEquals("left && right", printBinary(left, Operator.AND, right)),
            () -> assertEquals("left || right", printBinary(left, Operator.OR, right)),
            () -> assertEquals("left == right", printBinary(left, Operator.EQUAL, right)),
            () -> assertEquals("left != right", printBinary(left, Operator.NOT_EQUAL, right)),
            () -> assertEquals("left < right", printBinary(left, Operator.LESS, right)),
            () -> assertEquals("left <= right", printBinary(left, Operator.LESS_EQUAL, right)),
            () -> assertEquals("left > right", printBinary(left, Operator.GREATER, right)),
            () -> assertEquals("left >= right", printBinary(left, Operator.GREATER_EQUAL, right))
        );
    }

    @Test
    void printsNestedBinaryExpressions() {
        Expr expression = new Expr.Binary(
            new Expr.Binary(new Expr.Literal(10), Operator.MULTIPLY, new Expr.Literal(2)),
            Operator.MINUS,
            new Expr.Literal(5)
        );

        assertEquals("10 * 2 - 5", printer.print(expression));
    }

    @Test
    void printsUnaryExpressionsAndParenthesizesBinaryOperands() {
        assertAll(
            () -> assertEquals("-counter", printer.print(new Expr.Unary(
                Operator.MINUS, new Expr.Variable("counter")))),
            () -> assertEquals("!flag", printer.print(new Expr.Unary(
                Operator.NOT, new Expr.Variable("flag")))),
            () -> assertEquals("-(counter + 1)", printer.print(new Expr.Unary(
                Operator.MINUS,
                new Expr.Binary(new Expr.Variable("counter"), Operator.PLUS, new Expr.Literal(1)))))
        );
    }

    @Test
    void printsNestedUnaryExpressions() {
        Expr expression = new Expr.Unary(
            Operator.NOT,
            new Expr.Unary(Operator.NOT, new Expr.Variable("flag"))
        );

        assertEquals("!!flag", printer.print(expression));
    }

    @Test
    void printsCallsWithZeroOneAndMixedArguments() {
        assertAll(
            () -> assertEquals("reset()", printer.print(new Expr.Call(
                new Expr.Variable("reset"), Arrays.asList()))),
            () -> assertEquals("square(5)", printer.print(new Expr.Call(
                new Expr.Variable("square"), Arrays.asList(new Expr.Literal(5))))),
            () -> assertEquals("combine(x, 1 + 2, !flag, nested())", printer.print(new Expr.Call(
                new Expr.Variable("combine"),
                Arrays.asList(
                    new Expr.Variable("x"),
                    new Expr.Binary(new Expr.Literal(1), Operator.PLUS, new Expr.Literal(2)),
                    new Expr.Unary(Operator.NOT, new Expr.Variable("flag")),
                    new Expr.Call(new Expr.Variable("nested"), Arrays.asList())
                ))))
        );
    }

    private String printBinary(Expr left, Operator operator, Expr right) {
        return printer.print(new Expr.Binary(left, operator, right));
    }
}