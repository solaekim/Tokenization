import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {
    private final List<Token> tokens;
    private int position = 0;
    private final Map<String, Integer> variables = new HashMap<>();

    public Interpreter(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void interpretTheTokens() {
        while (!match(TokenType.EOF)) { // EOF token is the end of the token list so process until EOF token is found
            parseStatement(); // Parse one statement at a time
        }
        for (Map.Entry<String, Integer> entry : variables.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue()); // Print the variable name and its value
        }
    }

    private void parseStatement() {
        String V_Name = consume(TokenType.variable).value; // consume a variable token
        consume(TokenType.assign); // consume the assignment operator
        int value = parseExpression(); // parse the expression
        consume(TokenType.semicolon); // consume the semicolon
        variables.put(V_Name, value); // store the value of the variable in the map
    }

    private int parseExpression() {
        int value = parseTerm(); // parse the first term
        while (true) { // loop until no more terms are found
            if (match(TokenType.plus)) {  // if the next token is a plus sign
                value += parseTerm();
            } else if (match(TokenType.minus)) { // if the next token is a minus sign
                value -= parseTerm();
            } else {
                break;
            }
        }
        return value;
    }

    private int parseTerm() {
        int value = parseFactor(); // parse the first factor
        while (true) {
            if (match(TokenType.mult)) { // if the next token is a multiplication sign
                value *= parseFactor();
            } else if (match(TokenType.div)) { // if the next token is a division sign
                int divisor = parseFactor();
                if (divisor != 0) {
                    value /= divisor;
                } else {
                    // Exception will only occur when encounter division by zero
                    throw new RuntimeException("Division by zero");
                }
            } else {
                break;
            }
        }
        return value;
    }

    private int parseFactor() {
        if (match(TokenType.lparen)) {
            int value = parseExpression(); // parse the expression inside the parentheses
            consume(TokenType.rparen); // consume the closing parenthesis
            return value; // return the value of the expression
        } else if (match(TokenType.minus)) { // if the next token is a minus sign
            return -parseFactor(); // return the negative of the factor
        } else if (match(TokenType.plus)) { // if the next token is a plus sign
            return parseFactor();   // return the factor
        } else if (peek().type == TokenType.literal) { // if the next token is a literal
            return Integer.parseInt(consume(TokenType.literal).value); // return the value of the literal
        } else if (peek().type == TokenType.variable) { // if the next token is a variable
            String id = consume(TokenType.variable).value; // consume the variable token
            if (variables.containsKey(id)) { // check if the variable is initialized
                return variables.get(id); // return the value of the variable
            } else {
                throw new RuntimeException("Uninitialized variable: " + id); // throw an exception if the variable is not initialized
            }
        } else {
            throw new RuntimeException("Unexpected token: " + peek()); // throw an exception if the token is not recognized
        }
    }

    private Token peek() {
        return tokens.get(position);
    }

    private Token consume(TokenType type) {
        Token current = tokens.get(position);
        if (current.type == type) { // check if the current token matches the expected token
            position++;  // move to the next token
            return current; // return the current token
        }
        throw new RuntimeException("Error! Expected " + type + " but found " + current.type); // throw an exception if the token does not match the expected token
    }

    private boolean match(TokenType type) {
        if (tokens.get(position).type == type) { // check if the current token matches the expected token
            position++; // move to the next token
            return true;
        }
        return false;
    }
}
