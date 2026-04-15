package ast;

public class VariableNode extends ExpressionNode {
    public String name;

    public VariableNode(String name) {
        this.name = name;
    }
}
