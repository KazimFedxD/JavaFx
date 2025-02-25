package net.FedxD.JavaFx;

public class Node {
    public Position pos_start;
    public Position pos_end;

    public Node(Position pos_start, Position pos_end) {
        this.pos_start = pos_start;
        this.pos_end = pos_end;
    }
}

class NumberNode extends Node {
    public Token token;

    public NumberNode(Token token) {
        super(token.pos_start, token.pos_end);
        this.token = token;
    }

    public String toString() {
        return token.toString();
    }
}

class BinOpNode extends Node {
    public Node leftNode;
    public Token opToken;
    public Node rightNode;

    public BinOpNode(Node leftNode, Token opToken, Node rightNode) {
        super(leftNode.pos_start, rightNode.pos_end);
        this.leftNode = leftNode;
        this.opToken = opToken;
        this.rightNode = rightNode;
    }

    public String toString() {
        return String.format("(%s, %s, %s)", leftNode, opToken, rightNode);
    }
}

class UnaryOpNode extends Node {
    public Token opToken;
    public Node node;

    public UnaryOpNode(Token opToken, Node node) {
        super(opToken.pos_start, node.pos_end);
        this.opToken = opToken;
        this.node = node;
    }

    public String toString() {
        return String.format("(%s, %s)", opToken, node);
    }
}
