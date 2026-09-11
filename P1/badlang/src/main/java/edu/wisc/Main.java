package edu.wisc;

import java.util.List;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== BADLANG Language AST Pretty Printer Examples ===\n");
        
        PrettyPrinter printer = new PrettyPrinter();
        
        // Example 1: Simple arithmetic expressions
        System.out.println("1. Simple Arithmetic Expressions:");
        System.out.println("--------------------------------");
        
        Expr.Binary simpleAdd = new Expr.Binary(
            new Expr.Literal(5),
            Operator.PLUS,
            new Expr.Literal(3)
        );
        System.out.println("5 + 3 = " + printer.print(simpleAdd));
        
        Expr.Binary complexExpr = new Expr.Binary(
            new Expr.Binary(
                new Expr.Literal(10),
                Operator.MULTIPLY,
                new Expr.Literal(2)
            ),
            Operator.MINUS,
            new Expr.Literal(5)
        );
        System.out.println("(10 * 2) - 5 = " + printer.print(complexExpr));
        System.out.println();
        
        // Example 2: Variable declarations and assignments
        System.out.println("2. Variable Declarations and Assignments:");
        System.out.println("----------------------------------------");
        
        Stmt.Var intVar = new Stmt.Var(
            "x",
            VarType.INT,
            new Expr.Literal(42)
        );
        System.out.println("Variable declaration: " + printer.print(intVar));
        
        Stmt.Var boolVar = new Stmt.Var(
            "flag",
            VarType.BOOL,
            new Expr.Literal(true)
        );
        System.out.println("Variable declaration: " + printer.print(boolVar));
        
        Stmt.Assign assignment = new Stmt.Assign(
            "x",
            new Expr.Binary(
                new Expr.Variable("x"),
                Operator.PLUS,
                new Expr.Literal(1)
            )
        );
        System.out.println("Assignment: " + printer.print(assignment));
        System.out.println();
        
        // Example 3: Control flow statements
        System.out.println("3. Control Flow Statements:");
        System.out.println("---------------------------");
        
        Stmt.If ifStmt = new Stmt.If(
            new Expr.Binary(
                new Expr.Variable("x"),
                Operator.GREATER,
                new Expr.Literal(0)
            ),
            new Stmt.Print(new Expr.Variable("x")),
            new Stmt.Print(new Expr.Literal(0))
        );
        System.out.println("If statement: " + printer.print(ifStmt));
        
        Stmt.While whileStmt = new Stmt.While(
            new Expr.Binary(
                new Expr.Variable("i"),
                Operator.LESS,
                new Expr.Literal(10)
            ),
            new Stmt.Block(Arrays.asList(
                new Stmt.Print(new Expr.Variable("i")),
                new Stmt.Assign("i", new Expr.Binary(
                    new Expr.Variable("i"),
                    Operator.PLUS,
                    new Expr.Literal(1)
                ))
            ))
        );
        System.out.println("While loop: " + printer.print(whileStmt));
        System.out.println();
        
        // Example 4: Function definitions and calls
        System.out.println("4. Function Definitions and Calls:");
        System.out.println("---------------------------------");
        
        Stmt.Function factorial = new Stmt.Function(
            "factorial",
            VarType.INT,
            Arrays.asList(new Stmt.Parameter("n", VarType.INT)),
            Arrays.asList(
                new Stmt.If(
                    new Expr.Binary(
                        new Expr.Variable("n"),
                        Operator.LESS_EQUAL,
                        new Expr.Literal(1)
                    ),
                    new Stmt.Return(new Expr.Literal(1)),
                    new Stmt.Return(new Expr.Binary(
                        new Expr.Variable("n"),
                        Operator.MULTIPLY,
                        new Expr.Call(
                            new Expr.Variable("factorial"),
                            Arrays.asList(new Expr.Binary(
                                new Expr.Variable("n"),
                                Operator.MINUS,
                                new Expr.Literal(1)
                            ))
                        )
                    ))
                )
            )
        );
        System.out.println("Function definition:");
        System.out.println(printer.print(factorial));
        
        Expr.Call functionCall = new Expr.Call(
            new Expr.Variable("factorial"),
            Arrays.asList(new Expr.Literal(5))
        );
        System.out.println("Function call: " + printer.print(functionCall));
        System.out.println();
        
        // Example 5: Complex nested structures
        System.out.println("5. Complex Nested Structures:");
        System.out.println("-----------------------------");
        
        Stmt.Block complexBlock = new Stmt.Block(Arrays.asList(
            new Stmt.Var("sum", VarType.INT, new Expr.Literal(0)),
            new Stmt.Var("i", VarType.INT, new Expr.Literal(1)),
            new Stmt.While(
                new Expr.Binary(
                    new Expr.Variable("i"),
                    Operator.LESS_EQUAL,
                    new Expr.Literal(100)
                ),
                new Stmt.Block(Arrays.asList(
                    new Stmt.Assign("sum", new Expr.Binary(
                        new Expr.Variable("sum"),
                        Operator.PLUS,
                        new Expr.Variable("i")
                    )),
                    new Stmt.Assign("i", new Expr.Binary(
                        new Expr.Variable("i"),
                        Operator.PLUS,
                        new Expr.Literal(1)
                    ))
                ))
            ),
            new Stmt.Print(new Expr.Variable("sum"))
        ));
        System.out.println("Complex block:");
        System.out.println(printer.print(complexBlock));
        System.out.println();
        
        // Example 6: Logical expressions
        System.out.println("6. Logical Expressions:");
        System.out.println("----------------------");
        
        Expr.Binary logicalAnd = new Expr.Binary(
            new Expr.Binary(
                new Expr.Variable("x"),
                Operator.GREATER,
                new Expr.Literal(0)
            ),
            Operator.AND,
            new Expr.Binary(
                new Expr.Variable("x"),
                Operator.LESS,
                new Expr.Literal(100)
            )
        );
        System.out.println("Logical AND: " + printer.print(logicalAnd));
        
        Expr.Unary logicalNot = new Expr.Unary(
            Operator.NOT,
            new Expr.Variable("flag")
        );
        System.out.println("Logical NOT: " + printer.print(logicalNot));
        System.out.println();
        
        System.out.println("=== End of Examples ===");
        System.out.println("\nNote: All output is currently empty because the PrettyPrinter methods");
        System.out.println("are not yet implemented. Complete the TODO items in PrettyPrinter.java!");
    }
}