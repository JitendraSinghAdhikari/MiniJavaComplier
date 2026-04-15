package semantic;

import ast.*;
import java.util.HashSet;
import java.util.Set;

public class SemanticAnalyzer {

    private Set<String> declaredVariables = new HashSet<>();

    public void analyze(ProgramNode program) {

        for (ASTNode node : program.statements) {

            if (node instanceof AssignmentNode assignment) {

                String varName = assignment.variableName;

                if (declaredVariables.contains(varName)) {
                    throw new RuntimeException(
                        "Semantic Error: Variable '" + varName + "' already declared."
                    );
                }

                declaredVariables.add(varName);
            }

            if (node instanceof PrintNode printNode) {

                if (printNode.expression instanceof VariableNode variable) {

                    if (!declaredVariables.contains(variable.name)) {
                        throw new RuntimeException(
                            "Semantic Error: Variable '" + variable.name + "' not declared."
                        );
                    }
                }
            }
        }
    }
}
