package ast;

public class BinaryOpNode extends ExpressionNode {
    public String operator;
    public ExpressionNode left;
    public ExpressionNode right;

    public BinaryOpNode(String operator, ExpressionNode left, ExpressionNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
}
