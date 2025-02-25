package net.FedxD.JavaFx;

class ParseResult {
    public Node node;
    public Exceptions error;
    private int lastRegisteredAdvanceCount = 0;
    private int toReverseCount = 0;
    private int advanceCount = 0;

    public void registerAdvancement() {
        lastRegisteredAdvanceCount = 1;
        advanceCount++;
    }

    public Node register(ParseResult res) {
        lastRegisteredAdvanceCount = res.advanceCount;
        advanceCount += res.advanceCount;
        if (res.error != null) error = res.error;
        return res.node;
    }

    public ParseResult success(Node node) {
        this.node = node;
        return this;
    }

    public ParseResult failure(Exceptions error) {
        if (this.error == null || lastRegisteredAdvanceCount == 0) {
            this.error = error;
        }
        return this;
    }

    public Node tryRegister(ParseResult res) {
        if (res.error != null) {
            toReverseCount = res.advanceCount;
            return null;
        }
        return this.register(res);
    }

}

public class Parser {

}
