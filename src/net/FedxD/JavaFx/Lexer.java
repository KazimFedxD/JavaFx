package net.FedxD.JavaFx;

import java.util.ArrayList;
import java.util.Arrays;

class LexerResult {
    public ArrayList<Token> tokens;
    public Exceptions error;

    public LexerResult(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public LexerResult(Exceptions error) {
        this.error = error;
    }
}

class Token {
    public String type;
    public String value;
    public Position pos_start;
    public Position pos_end;

    public Token(String type, String value, Position pos_start, Position pos_end) {
        this.type = type;
        this.value = value;
        this.pos_start = pos_start;
        this.pos_end = pos_end;
    }

    public Token(String type, Position pos_start){
        this.type = type;
        this.pos_start = pos_start;
        this.pos_end = pos_start.copy();
    }

    public Token(String type){
        this.type = type;
    }

    public Token(String type, String value){
        this.type = type;
        this.value = value;
    }

    public boolean matches(String type, String value) {
        return this.type == type && this.value == value;
    }

    public String toString() {
        if (this.value != null) {
            return this.type + ":" + this.value;
        } else {
            return this.type;
        }
    }
}

public class Lexer {
    private final String text;
    private final Position pos;
    private char currentChar;
    final private String DIGITS = "0123456789";
    final private String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
    final private String[] KEYWORDS = {
            "if",
            "else",
            "for",
            "while",
            "func",
            "return",
            "break",
            "continue",
            "and",
            "or",
            "not",
    };


    public Lexer(String text, String filename) {
        this.text = text;
        this.pos = new Position(-1, 0, -1, filename, this.text);
        this.advance();
    }

    private void advance() {
        this.pos.advance();
        if (this.pos.idx < this.text.length()) {
            this.currentChar = this.text.charAt(this.pos.idx);
        } else {
            this.currentChar = '\0';
        }
    }

    public LexerResult makeTokens() {
        ArrayList<Token> tokens = new ArrayList<Token>();

        while (this.currentChar != '\0') {
            if (this.currentChar == ' ' || this.currentChar == '\t') {
                //tokens.add(new Token("INDENT", this.pos));
                this.advance();
            } else if (this.currentChar == '\n' || this.currentChar == ';'){
                tokens.add(new Token("NEWLINE", this.pos));
                this.advance();
            } else if (this.DIGITS.contains(String.valueOf(this.currentChar))){
                tokens.add(this.makeNumber());
            } else if (this.LETTERS.contains(String.valueOf(this.currentChar))) {
                tokens.add(this.makeIdentifier());
            } else if (this.currentChar == '"' || this.currentChar == '\''){
                tokens.add(this.get_string());
            } else if (this.currentChar == '#') {
                this.skipComment();
            } else if (this.currentChar == '+') {
                tokens.add(new Token("PLUS", this.pos));
                this.advance();
            } else if (this.currentChar == '-') {
                tokens.add(new Token("MINUS", this.pos));
                this.advance();
            } else if (this.currentChar == '*') {
                tokens.add(new Token("MUL", this.pos));
                this.advance();
            } else if (this.currentChar == '/') {
                tokens.add(new Token("DIV", this.pos));
                this.advance();
            } else if (this.currentChar == '(') {
                tokens.add(new Token("LPAREN", this.pos));
                this.advance();
            } else if (this.currentChar == ')') {
                tokens.add(new Token("RPAREN", this.pos));
                this.advance();
            } else if (this.currentChar == '^') {
                tokens.add(new Token("POW", this.pos));
                this.advance();
            } else {
                Position pos_start = this.pos.copy();
                char currentChar = this.currentChar;
                this.advance();
                return new LexerResult(new IllegalCharException(pos_start, this.pos, "'" + currentChar + "'"));
            }
        }
        tokens.add(new Token("EOF", this.pos));

        return new LexerResult(tokens);
    }

    private Token makeNumber() {
        String numStr = "";
        int dotCount = 0;
        Position pos_start = this.pos.copy();

        while (this.currentChar != '\0' && (this.currentChar == '.' || this.DIGITS.contains(String.valueOf(this.currentChar)))) {
            if (this.currentChar == '.') {
                if (dotCount == 1) break;
                dotCount++;
                numStr += '.';
            } else {
                numStr += this.currentChar;
            }
            this.advance();
        }

        if (numStr.startsWith(".")) {
            numStr = "0" + numStr;
        }
        if (numStr.endsWith(".")) {
            numStr += "0";
        }
        if (dotCount == 0) {
            return new Token("INT", numStr, pos_start, this.pos);
        } else {
            return new Token("FLOAT", numStr, pos_start, this.pos);
        }
    }

    private Token makeIdentifier() {
        String idStr = "";
        Position pos_start = this.pos.copy();

        String LETTERS_DIGITS = LETTERS + DIGITS;
        while (this.currentChar != '\0' && (LETTERS_DIGITS.contains(String.valueOf(this.currentChar)))) {
            idStr += this.currentChar;
            this.advance();
        }

        if (Arrays.asList(this.KEYWORDS).contains(idStr)) {
            return new Token("KEYWORD", idStr, pos_start, this.pos);
        } else {
            return new Token("IDENTIFIER", idStr, pos_start, this.pos);
        }
    }

    private void skipComment() {
        this.advance();

        while (this.currentChar != '\0' && this.currentChar != '\n') {
            this.advance();
        }

        this.advance();
    }

    private Token get_string() {
        String result = "";
        char quote = this.currentChar;
        boolean escape = false;
        Position pos_start = this.pos.copy();
        this.advance();
        while (this.currentChar != '\0' && (this.currentChar != quote || escape)) {
            if (escape) {
                if (this.currentChar == 'n') {
                    result += '\n';
                } else if (this.currentChar == 't') {
                    result += '\t';
                } else if (this.currentChar == 'r') {
                    result += '\r';
                } else if (this.currentChar == quote) {
                    result += quote;
                } else if (this.currentChar == '\\') {
                    result += '\\';
                } else {
                    result += this.currentChar;
                }
                escape = false;
            } else if (this.currentChar == '\\') {
                escape = true;
            } else {
                result += this.currentChar;
            }
            this.advance();
        }
        this.advance();
        return new Token("STRING", result, pos_start, this.pos);
    }
}




