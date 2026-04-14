import gen.*;
import org.antlr.v4.runtime.*;

import java.io.*;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java Main <source.pas> [-o output.ll]");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = inputPath.replaceFirst("\\.pas$", ".ll");
        for (int i = 1; i < args.length - 1; i++) {
            if (args[i].equals("-o")) outputPath = args[i + 1];
        }

        String source = new String(Files.readAllBytes(Paths.get(inputPath)));

        CharStream input = CharStreams.fromString(source);
        delphiLexer lexer = new delphiLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        delphiParser parser = new delphiParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> r, Object sym, int line, int col,
                                    String msg, RecognitionException e) {
                System.err.println("[Parse Error] line " + line + ":" + col + " " + msg);
            }
        });

        delphiParser.ProgramContext tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.err.println("Compilation aborted due to parse errors.");
            System.exit(1);
        }

        ConstantFolder folder = new ConstantFolder();
        folder.visit(tree);

        LLVMCodeGenerator codegen = new LLVMCodeGenerator(folder);
        codegen.visit(tree);
        String ir = codegen.emit();

        Files.writeString(Paths.get(outputPath), ir);
        System.err.println("Wrote " + outputPath);
    }
}
