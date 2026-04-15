package ast;

public class PrintNode extends StatementNode {
    public ExpressionNode expression;

    public PrintNode(ExpressionNode expression) {
        this.expression = expression;
    }
}
