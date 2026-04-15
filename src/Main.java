import java.util.List;

import ast.ProgramNode;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import tac.TACGenerator;

public class Main {

    public static void main(String[] args) {

        String code = """
                    int a = 10;
                    print(a);
                """;

        try {
            // ===== LEXER =====
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();

            System.out.println("TOKENS:");
            for (Token t : tokens) {
                System.out.println(t);
            }

            // ===== PARSER =====
            Parser parser = new Parser(tokens);
            ProgramNode program = parser.parseProgram(); // FIXED LINE

            System.out.println("\nSYNTAX OK");
            System.out.println("AST successfully generated!");

            // ===== TAC GENERATION =====
            TACGenerator tac = new TACGenerator();
            String tacCode = tac.generate(program);
            System.out.println("\nTHREE-ADDRESS CODE:");
            System.out.println(tacCode);

        } catch (Exception e) {
            System.out.println("\nERROR:");
            System.out.println(e.getMessage());
        }
    }
}
