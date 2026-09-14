package edu.wisc;

public class PrettyPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {
    private int indentLevel = 0;
    private static final String INDENT = "  ";

    public String print(Expr expr) {
        return expr.accept(this);
    }

    public String print(Stmt stmt) {
        return stmt.accept(this);
    }

    private String indent() {
        return INDENT.repeat(indentLevel);
    }

    // TODO: Implement all expression visitor methods
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        if (expr == null){
            return "";
        }

        // Recursively search the left side of the top level binary expression
        // and build the string of the left side
        StringBuilder binaryS = new StringBuilder("");
        if(expr.left instanceof Expr.Binary){
            binaryS.append(visitBinaryExpr((Expr.Binary)expr.left));
        }
        else if(expr.left instanceof Expr.Literal){
            binaryS.append(visitLiteralExpr((Expr.Literal)expr.left));
        }
        else if(expr.left instanceof Expr.Unary){
            binaryS.append(visitUnaryExpr((Expr.Unary)expr.left));
        }
        else if(expr.left instanceof Expr.Variable){
            binaryS.append(visitVariableExpr((Expr.Variable)expr.left));
        }
        else if(expr.left instanceof Expr.Call){
            binaryS.append(visitCallExpr((Expr.Call)expr.left));
        }

        // Add top level operator to string
        String opSym = expr.operator.getSymbol();
        binaryS.append(" ");
        binaryS.append(opSym);
        binaryS.append( " ");

        // Recursively search the right side of the top level binary expression
        // and build the string of the right side
        if(expr.right instanceof Expr.Binary){
            binaryS.append(visitBinaryExpr((Expr.Binary)expr.right));
        }
        else if(expr.right instanceof Expr.Literal){
            binaryS.append(visitLiteralExpr((Expr.Literal)expr.right));
        }
        else if(expr.right instanceof Expr.Unary){
            binaryS.append(visitUnaryExpr((Expr.Unary)expr.right));
        }
        else if(expr.right instanceof Expr.Variable){
            binaryS.append(visitVariableExpr((Expr.Variable)expr.right));
        }
        else if(expr.right instanceof Expr.Call){
            binaryS.append(visitCallExpr((Expr.Call)expr.right));
        }
        return binaryS.toString();
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if(expr == null){
            return "";
        }
        
        // Handle the case that the Literal is an Integer
        if(expr.value instanceof Integer){
            return expr.value.toString();
        }
        // Handle the case that the Literal is a Boolean
        else if(expr.value instanceof Boolean){
            if(Boolean.TRUE.equals(expr.value)){
                return "true";
            }
            else{
                return "false";
            }
        }
        else{
            return "";
        }
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        if(expr == null){
            return "";
        }
        StringBuilder unaryS = new StringBuilder("");
        String op = expr.operator.getSymbol();
        unaryS.append(op);

        if(expr.right instanceof Expr.Binary){
            unaryS.append("(");
            unaryS.append(visitBinaryExpr((Expr.Binary)expr.right));
            unaryS.append(")");
        }
        else if(expr.right instanceof Expr.Literal){
            unaryS.append(visitLiteralExpr((Expr.Literal)expr.right));
        }
        else if(expr.right instanceof Expr.Unary){
            //TODO: ASK PROF IF THIS SHOULD BE INCLUDED
            //unaryS.append(visitUnaryExpr((Expr.Unary)expr.right));
        }
        else if(expr.right instanceof Expr.Variable){
            unaryS.append(visitVariableExpr((Expr.Variable)expr.right));
        }
        else if(expr.right instanceof Expr.Call){
            unaryS.append(visitCallExpr((Expr.Call)expr.right));
        }
        return unaryS.toString();
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        // TODO: Implement variable printing
        if(expr == null){
            return "";
        }

        return expr.name;
    }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        // TODO: Implement function call printing
        // Example: functionName(arg1, arg2, arg3)
        if(expr == null){
            return "";
        }

        StringBuilder callS = new StringBuilder();

        if(expr.callee instanceof Expr.Variable){
            callS.append(visitVariableExpr((Expr.Variable) expr.callee));
        }

        callS.append("(");

        int firstRun = 1;
        for(Expr e : expr.arguments){
            // I want to avoid placing a comma at the start of the 
            // outer parantheses while also not placing a comma after the
            // last argument
            if(firstRun == 1){
                firstRun = 0;
            }
            else{
                callS.append(", ");
            }

            if(e instanceof Expr.Binary){
                callS.append(visitBinaryExpr((Expr.Binary)e));
            }
            else if(e instanceof Expr.Literal){
                callS.append(visitLiteralExpr((Expr.Literal)e));
            }
            else if(e instanceof Expr.Unary){
                callS.append(visitUnaryExpr((Expr.Unary)e));
            }
            else if(e instanceof Expr.Variable){
                callS.append(visitVariableExpr((Expr.Variable)e));
            }
            else if(e instanceof Expr.Call){
                callS.append(visitCallExpr((Expr.Call)e));
            }
        }

        callS.append(")");
        return callS.toString();
    }

    // TODO: Implement all statement visitor methods
    @Override
    public String visitReturnStmt(Stmt.Return stmt) {
        // TODO: Implement return statement printing
        // Example: return expression; or return;
        return "";
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        // TODO: Implement print statement printing
        // Example: print expression;
        return "";
    }

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        // TODO: Implement block printing with proper indentation
        // Example:
        // {
        //   statement1;
        //   statement2;
        // }
        return "";
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        // TODO: Implement if statement printing
        // Example: if (condition) thenBranch else elseBranch
        return "";
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        // TODO: Implement variable declaration printing
        // Example: int variableName = initialValue; or bool flag;
        return "";
    }

    @Override
    public String visitWhileStmt(Stmt.While stmt) {
        // TODO: Implement while loop printing
        // Example: while (condition) body
        return "";
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        // TODO: Implement expression statement printing
        // Example: expression;
        return "";
    }

    @Override
    public String visitFunctionStmt(Stmt.Function stmt) {
        // TODO: Implement function printing
        // Example:
        // fun int functionName(int param1, bool param2) {
        //   body statements
        // }
        return "";
    }

    @Override
    public String visitAssignStmt(Stmt.Assign stmt) {
        // TODO: Implement assignment printing
        // Example: variableName = expression;
        return "";
    }
} 