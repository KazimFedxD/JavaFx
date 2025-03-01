package net.FedxD.JavaFx;

import java.util.ArrayList;
import java.util.function.Function;

@FunctionalInterface
interface ParseFunction {
    ParseResult apply();
}

class ParseResult {
    public Node node;
    public Exceptions error;
    private int lastRegisteredAdvanceCount = 0;
    private int toReverseCount = 0;
    private int advanceCount = 0;

    public void registerAdvancement() {
        this.lastRegisteredAdvanceCount = 1;
        this.advanceCount++;
    }

    public Node register(ParseResult res) {
        this.lastRegisteredAdvanceCount = res.advanceCount;
        this.advanceCount += res.advanceCount;
        if (res.error != null) this.error = res.error;
        return res.node;
    }

    public ParseResult success(Node node) {
        this.node = node;
        return this;
    }

    public ParseResult failure(Exceptions error) {
        if (this.error == null || this.lastRegisteredAdvanceCount == 0) {
            this.error = error;
        }
        return this;
    }

    public Node tryRegister(ParseResult res) {
        if (res.error != null) {
            this.toReverseCount = res.advanceCount;
            return null;
        }
        return this.register(res);
    }

}

public class Parser {
    private final ArrayList<Token> tokens;
    private int tokenIndex = -1;
    private Token currentToken;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.advance();
    }

    private void skipIndent() {
        while (this.currentToken.type == "INDENT") {
            this.advance();
        }
    }

    private void advance() {
        this.tokenIndex++;
        if (this.tokenIndex < this.tokens.size()) {
            this.currentToken = this.tokens.get(this.tokenIndex);
        }
    }

    public ParseResult parse() {
        ParseResult res = this.expr();
        if (res.error == null && this.currentToken.type != "EOF") {
            return res.failure(new InvalidSyntaxException(this.currentToken.pos_start, this.currentToken.pos_end, "Expected '+', '-', '*', or '/'"));
        }
        return res;
    }


    private ParseResult factor() {
        ParseResult res = new ParseResult();
        Token token = this.currentToken;

        if (checkToken(new Token("PLUS"), new Token("MINUS"))) {
            res.registerAdvancement();
            this.advance();
            Node factor = res.register(this.factor());
            if (res.error != null) return res;
            return res.success(new UnaryOpNode(token, factor));
        } else if (checkToken(new Token("INT"))) {
            res.registerAdvancement();
            this.advance();
            return res.success(new NumberNode(token));
        } else if (checkToken(new Token("FLOAT"))) {
            res.registerAdvancement();
            this.advance();
            return res.success(new FloatNode(token));
        } else if (token.type == "LPAREN") {
            res.registerAdvancement();
            this.advance();
            Node expr = res.register(this.expr());
            if (res.error != null) return res;
            if (this.currentToken.type == "RPAREN") {
                res.registerAdvancement();
                this.advance();
                return res.success(expr);
            } else {
                return res.failure(new InvalidSyntaxException(token.pos_start, this.currentToken.pos_end.advance(), "Expected ')'"));
            }
        } else {
            return res.failure(new InvalidSyntaxException(token.pos_start, token.pos_end.advance(), "Expected int or float"));
        }
    }

    private ParseResult power() {
        return this.binOP(this::factor, new Token("POW"));
    }

    private ParseResult term() {
        return this.binOP(this::power, new Token("MUL"), new Token("DIV"));
    }

    private ParseResult expr() {
        return this.binOP(this::term, new Token("PLUS"), new Token("MINUS"));
    }

    private boolean checkToken(Token... ops) {
        for (Token op : ops) {
            if (op.value != null) {
                if (this.currentToken.type == op.type && this.currentToken.value == op.value) {
                    return true;
                }
            } else {
                if (this.currentToken.type == op.type) {
                    return true;
                }
            }
        }
        return false;
    }

    private ParseResult binOP(ParseFunction func, Token... ops) {
        ParseResult res = new ParseResult();
        Node left = res.register(func.apply());
        if (res.error != null) return res;


        while (checkToken(ops)) {
            Token opToken = this.currentToken;
            res.registerAdvancement();
            this.advance();
            Node right = res.register(func.apply());
            if (res.error != null) return res;
            left = new BinOpNode(left, opToken, right);
        }
        return res.success(left);

    }

    private ParseResult binOP(ParseFunction func, ParseFunction funcB, Token... ops) {
        ParseResult res = new ParseResult();
        Node left = res.register(func.apply());
        if (res.error != null) return res;


        while (checkToken(ops)) {
            Token opToken = this.currentToken;
            res.registerAdvancement();
            this.advance();
            Node right = res.register(funcB.apply());
            if (res.error != null) return res;
            left = new BinOpNode(left, opToken, right);
        }
        return res.success(left);

    }



}
