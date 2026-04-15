package ast;

public class NumberNode extends ExpressionNode {
    public int value;

    public NumberNode(int value) {
        this.value = value;
    }
}
