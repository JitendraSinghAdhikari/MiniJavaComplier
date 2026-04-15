package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {

        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char current = input.charAt(pos);

            // ===== WHITESPACE =====
            if (Character.isWhitespace(current)) {
                pos++;
                continue;
            }

            // ===== NUMBER =====
            if (Character.isDigit(current)) {
                tokens.add(new Token("NUMBER", readNumber()));
                continue;
            }

            // ===== IDENTIFIER / KEYWORD =====
            if (Character.isLetter(current)) {
                String word = readWord();

                if (word.equals("int")) {
                    tokens.add(new Token("INT", word));
                } else {
                    tokens.add(new Token("IDENTIFIER", word));
                }
                continue;
            }

            // ===== SYMBOLS =====
            switch (current) {

                case '=' -> tokens.add(new Token("ASSIGN", "="));

                case '+' -> tokens.add(new Token("PLUS", "+"));   // 🔥 ADDED

                case '-' -> tokens.add(new Token("MINUS", "-")); // (future use)

                case '*' -> tokens.add(new Token("MUL", "*"));   // (future use)

                case '/' -> tokens.add(new Token("DIV", "/"));   // (future use)

                case ';' -> tokens.add(new Token("SEMICOLON", ";"));

                case '(' -> tokens.add(new Token("LPAREN", "("));

                case ')' -> tokens.add(new Token("RPAREN", ")"));

                case '.' -> tokens.add(new Token("DOT", "."));

                default -> {
                    throw new RuntimeException("Unexpected character: " + current);
                }
            }

            pos++;
        }

        tokens.add(new Token("EOF", "EOF"));
        return tokens;
    }

    // ===================== HELPERS =====================

    private String readNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos++));
        }
        return sb.toString();
    }

    private String readWord() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() &&
               (Character.isLetterOrDigit(input.charAt(pos)))) { // 🔥 IMPROVED
            sb.append(input.charAt(pos++));
        }
        return sb.toString();
    }
}