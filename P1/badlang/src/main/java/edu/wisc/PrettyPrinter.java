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

        StringBuilder binaryS = new StringBuilder("");
        binaryS.append(expr.left.accept(this));

        // Add top level operator to string
        String opSym = expr.operator.getSymbol();
        binaryS.append(" ");
        binaryS.append(opSym);
        binaryS.append( " ");

        binaryS.append(expr.right.accept(this));
        return binaryS.toString();
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if(expr == null){
            return "";
        }
        
        if(expr.value instanceof Integer || expr.value instanceof Boolean){
            return expr.value.toString();
        }

        return "";
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        if(expr == null){
            return "";
        }
        String operand = expr.right == null ? "" : expr.right.accept(this);
        if(expr.right instanceof Expr.Binary){
            operand = "(" + operand + ")";
        }

        return expr.operator.getSymbol() + operand;
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
        else{
            return "";
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

            callS.append(e.accept(this));
        }

        callS.append(")");
        return callS.toString();
    }

    // TODO: Implement all statement visitor methods
    @Override
    public String visitReturnStmt(Stmt.Return stmt) {
        // TODO: Implement return statement printing
        // Example: return expression; or return;
        if(stmt == null){
            return "";
        }

        if(stmt.value == null){
            return "return;";
        }

        return "return " + stmt.value.accept(this) + ";";
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        // TODO: Implement print statement printing
        // Example: print expression;
        if(stmt == null){
            return "";
        }
        return "print " + stmt.expression.accept(this) + ";";
    }

    @Override
    public String visitBlockStmt(Stmt.Block stmt) {
        // TODO: Implement block printing with proper indentation
        // Example:
        // {
        //   statement1;
        //   statement2;
        // }
        if(stmt == null){
            return "";
        }

        if(stmt.statements.isEmpty()){
            return "{}";
        }

        StringBuilder blockS = new StringBuilder();
        blockS.append("{\n");
        indentLevel++;
        for(Stmt s : stmt.statements){
            blockS.append(indent());
            blockS.append(s.accept(this));
            blockS.append("\n");
        }
        indentLevel--;
        blockS.append(indent()).append("}");
        return blockS.toString();
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        // TODO: Implement if statement printing
        // Example: if (condition) thenBranch else elseBranch
        if(stmt == null){
            return "";
        }

        StringBuilder ifS = new StringBuilder("if (")
            .append(stmt.condition.accept(this))
            .append(") {\n");
        indentLevel++;
        ifS.append(indent()).append(stmt.thenBranch.accept(this)).append("\n");
        indentLevel--;
        ifS.append(indent()).append("}");
        if(stmt.elseBranch == null){
            return ifS.toString();
        }

        ifS.append(" else {\n");
        indentLevel++;
        ifS.append(indent()).append(stmt.elseBranch.accept(this)).append("\n");
        indentLevel--;
        return ifS.append(indent()).append("}").toString();
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        // TODO: Implement variable declaration printing
        // Example: int variableName = initialValue; or bool flag;
        if(stmt == null){
            return "";
        }

        StringBuilder varS = new StringBuilder();
        varS.append(stmt.type.toString()).append(" ").append(stmt.name);
        if(stmt.initializer == null){
            varS.append(";");
            return varS.toString();
        }
        varS.append(" = ").append(stmt.initializer.accept(this)).append(";");
        return varS.toString();
    }

    @Override
    public String visitWhileStmt(Stmt.While stmt) {
        // TODO: Implement while loop printing
        // Example: while (condition) body
        if(stmt == null){
            return "";
        }

        StringBuilder whileS = new StringBuilder();
        whileS.append("while (").append(stmt.condition.accept(this)).append("){\n")
        .append(stmt.body.accept(this)).append("\n}");
        return whileS.toString();
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        // TODO: Implement expression statement printing
        // Example: expression;
        return stmt.expression.accept(this) + ";";
    }

    @Override
    public String visitFunctionStmt(Stmt.Function stmt) {
        // TODO: Implement function printing
        // Example:
        // fun int functionName(int param1, bool param2) {
        //   body statements
        // }
        StringBuilder funcS = new StringBuilder();
        funcS.append("fun ").append(stmt.returnType.toString()).append(" ");
        funcS.append(stmt.name).append("(");
        int firstRun = 1;
        for(Stmt.Parameter param : stmt.params){
            if(firstRun == 1){
                firstRun = 0;
            } else{
                funcS.append(",");
            }
            funcS.append(param.type().toString()).append(" ").append(param.name());
        }
        funcS.append(") {\n");
        indentLevel++;
        for(Stmt s : stmt.body){
            funcS.append(indent()).append(s.accept(this)).append("\n");
        }
        funcS.append(indent()).append("}");
        return "";
    }

    @Override
    public String visitAssignStmt(Stmt.Assign stmt) {
        // TODO: Implement assignment printing
        // Example: variableName = expression;
        return stmt.name + " " + "= " + stmt.value.accept(this);
    }
} 