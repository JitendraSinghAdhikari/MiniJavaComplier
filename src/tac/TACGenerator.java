package tac;

import java.util.List;

import ast.*;

public class TACGenerator {

    private StringBuilder tacCode = new StringBuilder();
    private int tempCount = 0;

    // 🔥 Generate new temporary variable (t1, t2, ...)
    private String newTemp() {
        return "t" + (++tempCount);
    }

    // ===================== MAIN GENERATE =====================

    public String generate(ProgramNode program) {

        tacCode.setLength(0); // reset
        tempCount = 0;

        List<ASTNode> statements = program.statements;

        for (ASTNode node : statements) {

            // ===== Assignment =====
            if (node instanceof AssignmentNode assign) {

                String result = genExpr(assign.expression);

                tacCode.append(assign.variableName)
                       .append(" = ")
                       .append(result)
                       .append("\n");
            }

            // ===== Print =====
            else if (node instanceof PrintNode print) {

                String result = genExpr(print.expression);

                tacCode.append("print ")
                       .append(result)
                       .append("\n");
            }
        }

        return tacCode.toString();
    }

    // ===================== EXPRESSION HANDLING =====================

    private String genExpr(ExpressionNode expr) {

        // ===== NUMBER =====
        if (expr instanceof NumberNode number) {
            return String.valueOf(number.value);
        }

        // ===== VARIABLE =====
        if (expr instanceof VariableNode variable) {
            return variable.name;
        }

        // ===== BINARY OPERATION =====
        if (expr instanceof BinaryOpNode bin) {

            String left = genExpr(bin.left);
            String right = genExpr(bin.right);

            String temp = newTemp();

            tacCode.append(temp)
                   .append(" = ")
                   .append(left)
                   .append(" ")
                   .append(bin.operator)
                   .append(" ")
                   .append(right)
                   .append("\n");

            return temp;
        }

        // ===== FALLBACK =====
        throw new RuntimeException("Unknown expression type");
    }
}