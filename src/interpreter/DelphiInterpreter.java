package interpreter;

import gen.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.nio.file.*;

public class DelphiInterpreter {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: DelphiInterpreter <source.pas>");
            System.exit(1);
        }

        String source = new String(Files.readAllBytes(Paths.get(args[0])));
        interpret(source);
    }

    public static void interpret(String source) {
        CharStream input = CharStreams.fromString(source);
        delphiLexer lexer = new delphiLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        delphiParser parser = new delphiParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg,
                                    RecognitionException e) {
                System.err.println("[Parse Error] line " + line + ":" + charPositionInLine + " " + msg);
            }
        });

        ParseTree tree = parser.program();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("Parsing failed. Aborting.");
            return;
        }

        DelphiVisitor visitor = new DelphiVisitor();
        try {
            visitor.visit(tree);
        } catch (Exception ex) {
            System.err.println("[Runtime Error] " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }
}
