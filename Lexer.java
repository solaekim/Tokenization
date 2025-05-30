import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String input;

    // Regex pattern to tokenize input: matches leading whitespace (ignored), then captures numeric literals (\d+), identifiers ([a-zA-Z_][a-zA-Z0-9_]*), or single character operators and symbols ([-+*/()=;]).

    private static final Pattern TokenPatterns = Pattern.compile(
        "\\s*(?:(\\d+)|([a-zA-Z_][a-zA-Z0-9_]*)|([-+*/()=;]))");

    Lexer(String input) {
        this.input = input;
    }

    List<Token> tokenizer() {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TokenPatterns.matcher(input);
        while (matcher.find()) {
            if (matcher.group(1) != null) {// Group 1 is the numbers
                String number = matcher.group(1);
                // Ensure that the number is either "0" or does not start with a "0" because numbers starting with "0" are invalid
                if (number.matches("0") || !number.startsWith("0")) {
                    tokens.add(new Token(TokenType.literal, number));
                } else {
                    throw new IllegalArgumentException("Syntax error: invalid number format '" + number + "'");
                }
            } else if (matcher.group(2) != null) {// Group 2 are the identifiers
                tokens.add(new Token(TokenType.variable, matcher.group(2))); // Add the identifier to the list of tokens
            } else if (matcher.group(3) != null) { // Group 3 are the operators and symbols
                switch (matcher.group(3).charAt(0)) {
                    case '+':// If the character is a '+', add a token of type plus
                        tokens.add(new Token(TokenType.plus, "+"));
                        break;
                    case '-': // If the character is a '-', add a token of type minus
                        tokens.add(new Token(TokenType.minus, "-"));
                        break;
                    case '*': // If the character is a '*', add a token of type mult
                        tokens.add(new Token(TokenType.mult, "*"));
                        break;
                    case '/': // If the character is a '/', add a token of type div
                        tokens.add(new Token(TokenType.div, "/"));
                        break;
                    case '(': // If the character is a '(', add a token of type lparen
                        tokens.add(new Token(TokenType.lparen, "("));
                        break;
                    case ')': // If the character is a ')', add a token of type rparen
                        tokens.add(new Token(TokenType.rparen, ")"));
                        break;
                    case '=': // If the character is a '=', add a token of type assign
                        tokens.add(new Token(TokenType.assign, "="));
                        break;
                    case ';': // If the character is a ';', add a token of type semicolon
                        tokens.add(new Token(TokenType.semicolon, ";"));
                        break;
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, "")); // Add an EOF token to the end of the list of tokens
        return tokens;
    }
}
