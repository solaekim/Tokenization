enum TokenType {
    variable, literal, plus, minus, mult, div, lparen, rparen, assign, semicolon, EOF, invalid
}

public class Token {
    TokenType type;
    String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
}
