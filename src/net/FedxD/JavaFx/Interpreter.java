package net.FedxD.JavaFx;

import java.lang.reflect.Method;

class Context {
    public String displayName;
    public Context parent;
    public Node displayNode;
    public Position parentEntryPos;

    public Context(String displayName, Context parent, Node displayNode, Position parentEntryPos) {
        this.displayName = displayName;
        this.parent = parent;
        this.displayNode = displayNode;
        this.parentEntryPos = parentEntryPos;
    }

    public Context(String displayName, Context parent, Node displayNode) {
        this.displayName = displayName;
        this.parent = parent;
        this.displayNode = displayNode;
    }

    public Context(String displayName, Context parent) {
        this.displayName = displayName;
        this.parent = parent;
    }
    public Context(String displayName) {
        this.displayName = displayName;
    }

    public Context copy() {
        return new Context(displayName, parent, displayNode, parentEntryPos);
    }
}

class RunTimeResult {
    public Values value;
    public Exceptions error;

    public void reset() {
        this.value = null;
        this.error = null;
    }

    public RunTimeResult(){
    }

    public Values register(RunTimeResult res) {
        if (res.error != null) this.error = res.error;
        return res.value;
    }

    public RunTimeResult success(Values value) {
        this.reset();
        this.value = value;
        return this;
    }

    public RunTimeResult failure(Exceptions error) {
        this.reset();
        this.error = error;
        return this;
    }

}

public class Interpreter {
    private final Context context;

    public Interpreter(Context context) {
        this.context = context;
    }

    public RunTimeResult visit(Node node, Context context) {
        String methodName = "visit_" + node.getClass().getSimpleName();
        try {
            Method method = this.getClass().getMethod(methodName, node.getClass(), Context.class);
            Object res =  method.invoke(this, node, context);
            if (res instanceof RunTimeResult) {
                return (RunTimeResult) res;
            }
            return new RunTimeResult();
        } catch (NoSuchMethodException e) {
            return this.noVisitMethod(node);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public RunTimeResult visit(Node node) {
        return visit(node, this.context);
    }

    public RunTimeResult noVisitMethod(Node node) {
        System.out.println("No visit method defined for node: " + node.getClass().getSimpleName());
        System.exit(1);
        return null;
    }

    public RunTimeResult visit_NumberNode(NumberNode node, Context context) {
        return new RunTimeResult().success(new Number(Long.parseLong(node.token.value)).setContext(context).setPos(node.pos_start, node.pos_end));
    }

    public RunTimeResult visit_FloatNode(FloatNode node, Context context) {
        return new RunTimeResult().success(new Float(Double.parseDouble(node.token.value)).setContext(context).setPos(node.pos_start, node.pos_end));
    }

    public RunTimeResult visit_BinOpNode(BinOpNode node, Context context) {
        RunTimeResult res = new RunTimeResult();
        Values left = res.register(this.visit(node.leftNode, context));
        if (res.error != null) return res;
        Values right = res.register(this.visit(node.rightNode, context));
        if (res.error != null) return res;

        OperationResult result;

        if (node.opToken.type == "PLUS") {
            result = left.add(right);
        } else if (node.opToken.type == "MINUS") {
            result = left.sub(right);
        } else if (node.opToken.type == "MUL") {
            result = left.mul(right);
        } else if (node.opToken.type == "DIV") {
            result = left.div(right);
        } else if (node.opToken.type == "POW") {
            result = left.pow(right);
        } else if (node.opToken.type == "EE") {
            result = left.eq(right);
        } else if (node.opToken.type == "NE") {
            result = left.ne(right);
        } else if (node.opToken.type == "LT") {
            result = left.lt(right);
        } else if (node.opToken.type == "GT") {
            result = left.gt(right);
        } else if (node.opToken.type == "LTE") {
            result = left.lte(right);
        } else if (node.opToken.type == "GTE") {
            result = left.gte(right);
        } else if (node.opToken.matches("KEYWORD", "and")) {
            result = left.and(right);
        } else if (node.opToken.matches("KEYWORD", "or")) {
            result = left.or(right);
        } else if (node.opToken.type == "MOD") {
            result = left.mod(right);
        } else {
            return res.failure(new RunTimeError(node.pos_start, node.pos_end, "Invalid operation", context));
        }
        if (result.error != null) {
            return res.failure(result.error);
        }
        return res.success(result.value.setPos(node.pos_start, node.pos_end));
    }

    public RunTimeResult visit_UnaryOpNode(UnaryOpNode node, Context context) {
        RunTimeResult res = new RunTimeResult();
        Values number = res.register(this.visit(node.node, context));
        if (res.error != null) return res;

        OperationResult result;

        if (node.opToken.type == "PLUS") {
            result = new OperationResult(number);
        } else if (node.opToken.type == "MINUS") {
            result = number.mul(new Number(-1));
        } else if (node.opToken.matches("KEYWORD", "not")) {
            result = number.not();
        } else {
            return res.failure(new RunTimeError(node.pos_start, node.pos_end, "Invalid operation", context));
        }

        if (result.error != null) {
            return res.failure(result.error);
        }

        return res.success(result.value.setPos(node.pos_start, node.pos_end));
    }
}
