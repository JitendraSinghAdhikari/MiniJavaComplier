package gui;

import parser.Parser;
import ast.ProgramNode;
import lexer.Lexer;
import lexer.Token;
import semantic.SemanticAnalyzer;
import tac.TACGenerator;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class CompilerGUI extends JFrame {

    private JTextArea codeArea;
    private JTextArea tokenArea;
    private JTextArea errorArea;
    private JTextArea tacArea;
    private JTextArea aiArea;

    public CompilerGUI() {

        setTitle("AI Assisted Mini Java Compiler");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("AI Assisted Mini Java Compiler", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // ===== CODE AREA =====
        codeArea = new JTextArea("""
class Demo {
    public static void main(String[] args) {

        int a = 10;
        print(a);

    }
}
""");

        JScrollPane codeScroll = new JScrollPane(codeArea);
        codeScroll.setBorder(BorderFactory.createTitledBorder("Source Code"));

        // ===== TABS =====
        JTabbedPane tabbedPane = new JTabbedPane();

        tokenArea = new JTextArea();
        errorArea = new JTextArea();
        tacArea = new JTextArea();
        aiArea = new JTextArea();

        tabbedPane.add("Tokens", new JScrollPane(tokenArea));
        tabbedPane.add("Errors", new JScrollPane(errorArea));
        tabbedPane.add("TAC", new JScrollPane(tacArea));
        tabbedPane.add("AI Help", new JScrollPane(aiArea));

        // ===== SPLIT =====
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                codeScroll,
                tabbedPane
        );
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);

        // ===== BUTTON =====
        JButton compileButton = new JButton("Compile");
        compileButton.addActionListener(e -> compileCode());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(compileButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ===================== COMPILE =====================

    private void compileCode() {

    tokenArea.setText("");
    errorArea.setText("");
    tacArea.setText("");
    aiArea.setText("");

    try {

        String fullCode = codeArea.getText();

        // ===== Extract main body =====
        String extractedCode = extractMainBody(fullCode);

        System.out.println("===== EXTRACTED CODE =====");
        System.out.println(extractedCode);

        // ===== LEXICAL ANALYSIS =====
        Lexer lexer = new Lexer(extractedCode);
        List<Token> tokens = lexer.tokenize();

        // ✅ DEBUG TOKENS IN TERMINAL
        System.out.println("===== TOKENS =====");
        for (Token t : tokens) {
            System.out.println(t);
        }

        // ✅ SHOW TOKENS IN GUI
        StringBuilder tokenOutput = new StringBuilder();
        for (Token t : tokens) {
            tokenOutput.append(t).append("\n");
        }
        tokenArea.setText(tokenOutput.toString());

            // ===== SYNTAX ANALYSIS =====
            Parser parser = new Parser(tokens);
            ProgramNode program = parser.parseProgram();

            // ✅ DEBUG LINE
            System.out.println("Statements count: " + program.statements.size());

            // ===== SEMANTIC ANALYSIS =====
            SemanticAnalyzer semantic = new SemanticAnalyzer();
            semantic.analyze(program);

            // ===== TAC GENERATION =====
            TACGenerator generator = new TACGenerator();
            String tacCode = generator.generate(program);

            if (tacCode.isEmpty()) {
                tacArea.setText("⚠ No TAC generated.\nUse: print(a);");
            } else {
                tacArea.setText(
                        "Intermediate Representation (TAC)\n" +
                        "---------------------------------\n" +
                        tacCode
                );
            }

            // ===== SUCCESS =====
            errorArea.setText("✔ Compilation Successful");

            aiArea.setText("""
AI Assistant:
✔ Syntax correct
✔ Semantic check passed
✔ TAC generated successfully
""");

        } catch (Exception ex) {

            errorArea.setText("❌ " + ex.getMessage());
            tacArea.setText("");

            aiArea.setText("""
AI Suggestion:
→ Check semicolons (;)
→ Use print(a)
→ Declare variables before use
→ Check brackets { }
""");
        }
    }

    // ===================== MAIN BODY EXTRACTOR =====================

    private String extractMainBody(String code) {

        int mainStart = code.indexOf("main");
        if (mainStart == -1) {
            throw new RuntimeException("Main method not found.");
        }

        int braceStart = code.indexOf("{", mainStart);
        if (braceStart == -1) {
            throw new RuntimeException("Opening brace of main not found.");
        }

        int braceCount = 1;
        int i = braceStart + 1;

        while (i < code.length() && braceCount > 0) {
            if (code.charAt(i) == '{') braceCount++;
            else if (code.charAt(i) == '}') braceCount--;
            i++;
        }

        if (braceCount != 0) {
            throw new RuntimeException("Main method braces mismatch.");
        }

        return code.substring(braceStart + 1, i - 1).trim();
    }

    // ===================== MAIN =====================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CompilerGUI().setVisible(true);
        });
    }
}