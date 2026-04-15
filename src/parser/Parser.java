package parser;

import ast.*;
import java.util.ArrayList;
import lexer.Token;
import java.util.List;

public class Parser {

    private List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // ===================== PROGRAM =====================

    public ProgramNode parseProgram() {
        List<ASTNode> statements = new ArrayList<>();

        while (!isAtEnd()) {
            statements.add(parseStatement());
        }

        return new ProgramNode(statements);
    }

    // ===================== STATEMENT =====================

    private StatementNode parseStatement() {

        // int a = ...
        if (match("INT")) {
            return parseAssignment();
        }

        // print OR System.out.println
        if (check("IDENTIFIER")) {

            if (peek().value.equals("print")) {
                return parsePrint();
            }

            return parseSystemOutPrint();
        }

        throw error(peek(), "Invalid statement");
    }

    // ===================== ASSIGNMENT =====================

    private AssignmentNode parseAssignment() {

        Token nameToken = peek();
        consume("IDENTIFIER", "Expected variable name");
        consume("ASSIGN", "Expected '='");

        ExpressionNode expr = parseExpression();
        consume("SEMICOLON", "Expected ';'");

        return new AssignmentNode(nameToken.value, expr);
    }

    // ===================== PRINT =====================

    private PrintNode parsePrint() {

        consume("IDENTIFIER", "Expected 'print'");

        if (match("LPAREN")) {
            ExpressionNode expr = parseExpression();
            consume("RPAREN", "Expected ')'");
            consume("SEMICOLON", "Expected ';'");
            return new PrintNode(expr);
        }

        ExpressionNode expr = parseExpression();
        consume("SEMICOLON", "Expected ';'");

        return new PrintNode(expr);
    }

    // ===================== SYSTEM PRINT =====================

    private PrintNode parseSystemOutPrint() {

        consume("IDENTIFIER", "Expected 'System'");
        consume("DOT", "Expected '.'");
        consume("IDENTIFIER", "Expected 'out'");
        consume("DOT", "Expected '.'");
        consume("IDENTIFIER", "Expected 'println'");

        consume("LPAREN", "Expected '('");
        ExpressionNode expr = parseExpression();
        consume("RPAREN", "Expected ')'");
        consume("SEMICOLON", "Expected ';'");

        return new PrintNode(expr);
    }

    // ===================== EXPRESSIONS =====================

    private ExpressionNode parseExpression() {

        ExpressionNode left = parsePrimary();

        // 🔥 Handle binary operations (+ only for now)
        while (match("PLUS")) {
            ExpressionNode right = parsePrimary();
            left = new BinaryOpNode("+", left, right);
        }

        return left;
    }

    private ExpressionNode parsePrimary() {

        if (match("NUMBER")) {
            return new NumberNode(Integer.parseInt(previous().value));
        }

        if (match("IDENTIFIER")) {
            return new VariableNode(previous().value);
        }

        throw error(peek(), "Invalid expression");
    }

    // ===================== HELPERS =====================

    private boolean match(String type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private void consume(String type, String message) {
        if (check(type)) {
            advance();
            return;
        }
        throw error(peek(), message);
    }

    private boolean check(String type) {
        if (isAtEnd()) return false;
        return peek().type.equals(type);
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type.equals("EOF");
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private RuntimeException error(Token token, String message) {
        return new RuntimeException(
            "Syntax Error near '" + token.value + "': " + message
        );
    }
}