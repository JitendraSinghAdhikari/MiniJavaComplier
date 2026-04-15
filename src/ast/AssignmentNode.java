package ast;

public class AssignmentNode extends StatementNode {
    public String variableName;
    public ExpressionNode expression;

    public AssignmentNode(String variableName, ExpressionNode expression) {
        this.variableName = variableName;
        this.expression = expression;
    }
}
