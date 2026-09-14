package edu.wisc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void printsReturnsWithEveryExpressionShapeAndWithoutAValue() {
        assertAll(
            () -> assertEquals("return;", printer.print(new Stmt.Return(null))),
            () -> assertEquals("return 42;", printer.print(new Stmt.Return(
                new Expr.Literal(42)))),
            () -> assertEquals("return flag;", printer.print(new Stmt.Return(
                new Expr.Variable("flag")))),
            () -> assertEquals("return !flag;", printer.print(new Stmt.Return(
                new Expr.Unary(Operator.NOT, new Expr.Variable("flag"))))),
            () -> assertEquals("return total + 1;", printer.print(new Stmt.Return(
                new Expr.Binary(new Expr.Variable("total"), Operator.PLUS, new Expr.Literal(1))))),
            () -> assertEquals("return build(1, !ready);", printer.print(new Stmt.Return(
                new Expr.Call(
                    new Expr.Variable("build"),
                    Arrays.asList(
                        new Expr.Literal(1),
                        new Expr.Unary(Operator.NOT, new Expr.Variable("ready"))
                    )))))
        );
    }

    @Test
    void printsPrintStatementsWithEveryExpressionShape() {
        assertAll(
            () -> assertEquals("print 42;", printer.print(new Stmt.Print(
                new Expr.Literal(42)))),
            () -> assertEquals("print true;", printer.print(new Stmt.Print(
                new Expr.Literal(true)))),
            () -> assertEquals("print value;", printer.print(new Stmt.Print(
                new Expr.Variable("value")))),
            () -> assertEquals("print !ready;", printer.print(new Stmt.Print(
                new Expr.Unary(Operator.NOT, new Expr.Variable("ready"))))),
            () -> assertEquals("print count + 1;", printer.print(new Stmt.Print(
                new Expr.Binary(new Expr.Variable("count"), Operator.PLUS, new Expr.Literal(1))))),
            () -> assertEquals("print format(value, !debug);", printer.print(new Stmt.Print(
                new Expr.Call(
                    new Expr.Variable("format"),
                    Arrays.asList(
                        new Expr.Variable("value"),
                        new Expr.Unary(Operator.NOT, new Expr.Variable("debug"))
                    )))))
        );
    }

    @Test
    void printsEmptyAndMultipleStatementBlocks() {
        assertAll(
            () -> assertEquals("{}", printer.print(new Stmt.Block(Arrays.asList()))),
            () -> assertEquals("{\n  return 1;\n  print done;\n}", printer.print(
                new Stmt.Block(Arrays.asList(
                    new Stmt.Return(new Expr.Literal(1)),
                    new Stmt.Print(new Expr.Variable("done"))
                ))))
        );
    }

    @Test
    void increasesIndentationForNestedBlocks() {
        Stmt.Block block = new Stmt.Block(Arrays.asList(
            new Stmt.Block(Arrays.asList(
                new Stmt.Return(new Expr.Literal(1)),
                new Stmt.Print(new Expr.Variable("inner"))
            )),
            new Stmt.Print(new Expr.Variable("outer"))
        ));

        assertEquals(
            "{\n" +
            "  {\n" +
            "    return 1;\n" +
            "    print inner;\n" +
            "  }\n" +
            "  print outer;\n" +
            "}",
            printer.print(block)
        );
    }

    @Test
    void printsIfStatementsWithEveryExpressionCondition() {
        assertAll(
            () -> assertEquals(
                "if (true) {\n  print yes;\n}",
                printer.print(new Stmt.If(
                    new Expr.Literal(true),
                    new Stmt.Print(new Expr.Variable("yes")),
                    null))),
            () -> assertEquals(
                "if (ready) {\n  print yes;\n} else {\n  print no;\n}",
                printer.print(new Stmt.If(
                    new Expr.Variable("ready"),
                    new Stmt.Print(new Expr.Variable("yes")),
                    new Stmt.Print(new Expr.Variable("no"))))),
            () -> assertEquals(
                "if (!ready) {\n  return 1;\n}",
                printer.print(new Stmt.If(
                    new Expr.Unary(Operator.NOT, new Expr.Variable("ready")),
                    new Stmt.Return(new Expr.Literal(1)),
                    null))),
            () -> assertEquals(
                "if (count > 0) {\n  print count;\n}",
                printer.print(new Stmt.If(
                    new Expr.Binary(new Expr.Variable("count"), Operator.GREATER, new Expr.Literal(0)),
                    new Stmt.Print(new Expr.Variable("count")),
                    null))),
            () -> assertEquals(
                "if (isReady()) {\n  print started;\n}",
                printer.print(new Stmt.If(
                    new Expr.Call(new Expr.Variable("isReady"), Arrays.asList()),
                    new Stmt.Print(new Expr.Variable("started")),
                    null)))
        );
    }

    @Test
    void printsNestedIfBranchesWithStableIndentation() {
        Stmt.If nestedIf = new Stmt.If(
            new Expr.Variable("innerCondition"),
            new Stmt.Print(new Expr.Variable("innerThen")),
            new Stmt.Print(new Expr.Variable("innerElse"))
        );

        assertEquals(
            "if (outerCondition) {\n" +
            "  if (innerCondition) {\n" +
            "    print innerThen;\n" +
            "  } else {\n" +
            "    print innerElse;\n" +
            "  }\n" +
            "}",
            printer.print(new Stmt.If(
                new Expr.Variable("outerCondition"),
                nestedIf,
                null))
        );
    }

    private String printBinary(Expr left, Operator operator, Expr right) {
        return printer.print(new Expr.Binary(left, operator, right));
    }
}