package net.FedxD.JavaFx;



class Exceptions {
    Position pos_start;
    Position pos_end;
    String errorName;
    String details;

    public Exceptions(Position pos_start, Position pos_end, String errorName, String details) {
        this.pos_start = pos_start;
        this.pos_end = pos_end;
        this.errorName = errorName;
        this.details = details;
    }

    public Exceptions(String errorName, String details) {
        this.errorName = errorName;
        this.details = details;
    }

    public String toString() {
        if (pos_start == null || pos_end == null) {
            return String.format("%s: %s", errorName, details);
        }
        String result = String.format("\n%s: %s", errorName, details);
        result += String.format("\nFile %s, line %s", pos_start.fileName, pos_start.ln + 1);
        result += "\n\n" + Position.stringWithArrows(pos_start.fileText, pos_start, pos_end);
        return result;
    }
}

class InvalidSyntaxException extends Exceptions {
    public InvalidSyntaxException(Position pos_start, Position pos_end, String details) {
        super(pos_start, pos_end, "Invalid Syntax", details);
    }
}

class IllegalCharException extends Exceptions {
    public IllegalCharException(Position pos_start, Position pos_end, String details) {
        super(pos_start, pos_end, "Illegal Character", details);
    }
}

class ExpectedCharException extends Exceptions {
    public ExpectedCharException(Position pos_start, Position pos_end, String details) {
        super(pos_start, pos_end, "Expected Character", details);
    }
}

class RunTimeError extends Exceptions {
    public Context context;

    public RunTimeError(Position pos_start, Position pos_end, String details, Context context) {
        super(pos_start, pos_end, "Runtime Error", details);
        System.out.println(pos_start);
        this.context = context;
    }

    public String toString() {
        String result = this.generateTraceback();
        result += super.toString();
        return result;
    }

    public String generateTraceback() {
        String result = "";
        Position pos = this.pos_start.copy();
        Context ctx = this.context;
        while (ctx != null) {
            result = String.format("\nFile %s, line %s, in %s\n", pos.fileName, pos.ln + 1, ctx.displayName) + result;
            pos = ctx.parentEntryPos;
            ctx = ctx.parent;
        }
        return "Traceback (most recent call last):" + result;
    }
}
