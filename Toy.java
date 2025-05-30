import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Toy {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Toy <filename>");
            System.exit(1);
        }

        String filename = args[0];

        try {
            String input = new String(Files.readAllBytes(Paths.get(filename)));

            Lexer lexer = new Lexer(input);
            List<Token> tokens = lexer.tokenizer();
            Interpreter interpreter = new Interpreter(tokens);
            interpreter.interpretTheTokens();

        } catch (Exception e) {
            System.out.println("Error encountered: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Done! Program Exited Successfully.");
    }
}
