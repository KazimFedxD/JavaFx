package net.FedxD.JavaFx;

class OperationResult {
    public Values value;
    public Exceptions error;

    public OperationResult(Values value) {
        this.value = value;
    }

    public OperationResult(Exceptions error) {
        this.error = error;
    }
}

public class Values<T> {
    public T value;
    public Context context;
    public Position pos_start;
    public Position pos_end;

    public Values(Context context, Position pos_start, Position pos_end) {
        this.context = context;
        this.pos_start = pos_start;
        this.pos_end = pos_end;
    }

    public Values(Position pos_start, Position pos_end) {
        this.pos_start = pos_start;
        this.pos_end = pos_end;
    }

    public Values(Context context) {
        this.context = context;
    }

    public Values() {
    }

    public Values setPos(Position pos_start, Position pos_end) {
        this.pos_start = pos_start;
        this.pos_end = pos_end;
        return this;
    }

    public Values setContext(Context context) {
        this.context = context;
        return this;
    }

    public Values copy() {
        return new Values(context, pos_start, pos_end);
    }

    public String toString() {
        return String.format("%s", this.value);
    }

    public boolean isTrue() {
        return this.value != null;
    }

    public boolean isFalse() {
        return !isTrue();
    }

    private RunTimeError illegalValueOperation(Values other) {
        if (other == null) {
            other = this;
        }
        return new RunTimeError(this.pos_start, other.pos_end, "Illegal Operation", this.context);
    }

    public OperationResult add(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult sub(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult mul(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult div(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult pow(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult mod(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult eq(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult ne(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult lt(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult gt(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult lte(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult gte(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult and(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult or(Values other) {
        return new OperationResult(this.illegalValueOperation(other));
    }

    public OperationResult not() {
        return new OperationResult(this.illegalValueOperation(null));
    }
}

class Number extends Values<Long> {


    public Number(long value) {
        super();
        this.value = value;
    }

    public OperationResult add(Values other) {
        if (other instanceof Number) {
            return new OperationResult(new Number(this.value + ((Number) other).value).setContext(this.context));
        } else if (other instanceof Float) {
            return new OperationResult(new Float(this.value + ((Float) other).value).setContext(this.context));
        }
        return super.add(other);
    }

    public OperationResult sub(Values other) {
        if (other instanceof Number) {
            return new OperationResult(new Number(this.value - ((Number) other).value).setContext(this.context));
        } else if (other instanceof Float) {
            return new OperationResult(new Float(this.value - ((Float) other).value).setContext(this.context));
        }
        return super.sub(other);
    }

    public OperationResult mul(Values other) {
        if (other instanceof Number) {
            return new OperationResult(new Number(this.value * ((Number) other).value).setContext(this.context));
        } else if (other instanceof Float) {
            return new OperationResult(new Float(this.value * ((Float) other).value).setContext(this.context));
        }
        return super.mul(other);
    }

    public OperationResult div(Values other) {
        if (other instanceof Number) {
            if (((Number) other).value.equals(0)) {
                return new OperationResult(new RunTimeError(this.pos_start, this.pos_end, "Division by zero", this.context));
            }
            return new OperationResult(new Number(this.value / ((Number) other).value).setContext(this.context));
        } else if (other instanceof Float) {
            if (((Float) other).value.equals(0.0)) {
                return new OperationResult(new RunTimeError(this.pos_start, this.pos_end, "Division by zero", this.context));
            }
            return new OperationResult(new Float(this.value / ((Float) other).value).setContext(this.context));
        }
        return super.div(other);
    }

    public OperationResult pow(Values other) {
        if (other instanceof Number) {
            return new OperationResult(new Number((int) Math.pow(this.value, ((Number) other).value)).setContext(this.context));
        } else if (other instanceof Float) {
            return new OperationResult(new Float(Math.pow(this.value, ((Float) other).value)).setContext(this.context));
        }
        return super.pow(other);
    }

    public OperationResult mod(Values other) {
        if (other instanceof Number) {
            if (((Number) other).value == 0) {
                return new OperationResult(new RunTimeError(this.pos_start, this.pos_end, "Division by zero", this.context));
            }
            return new OperationResult(new Number(this.value % ((Number) other).value).setContext(this.context));
        } else if (other instanceof Float) {
            if (((Float) other).value == 0) {
                return new OperationResult(new RunTimeError(this.pos_start, this.pos_end, "Division by zero", this.context));
            }
            return new OperationResult(new Float(this.value % ((Float) other).value).setContext(this.context));
        }
        return super.mod(other);
    }

    public OperationResult lt(Values other) {
        if (other instanceof Number) {
            return new OperationResult(new Number(this.value < ((Number) other).value ? 1 : 0).setContext(this.context));
        } else if (other instanceof Float) {
            return new OperationResult(new Number(this.value < ((Float) other).value ? 1 : 0).setContext(this.context));
        }
        return super.lt(other);
    }

    public OperationResult gt(Values other) {
        if (other instanceof Number) {
            return new OperationResult(new Number(this.value > ((Number) other).value ? 1 : 0).setContext(this.context));
        } else if (other instanceof Float) {
            return new OperationResult(new Number(this.value > ((Float) other).value ? 1 : 0).setContext(this.context));
        }
        return super.gt(other);
    }

    public OperationResult lte(Values other) {
        if (other instanceof Number) {
            return new OperationResult(new Number(this.value <= ((Number) other).value ? 1 : 0).setContext(this.context));
        } else if (other instanceof Float) {
            return new OperationResult(new Number(this.value <= ((Float) other).value ? 1 : 0).setContext(this.context));
        }
        return super.lte(other);
    }

    public OperationResult gte(Values other) {
        if (other instanceof Number) {
            return new OperationResult(new Number(this.value >= ((Number) other).value ? 1 : 0).setContext(this.context));
        } else if (other instanceof Float) {
            return new OperationResult(new Number(this.value >= ((Float) other).value ? 1 : 0).setContext(this.context));
        }
        return super.gte(other);
    }

    public boolean isTrue() {
        return this.value != 0;
    }
}

class Float extends Values<Double> {

    public Float(double value) {
        super();
        this.value = value;
    }

    public OperationResult add(Values other) {
        if (other instanceof Float) {
            return new OperationResult(new Float(this.value + ((Float) other).value).setContext(this.context));
        } else if (other instanceof Number) {
            return new OperationResult(new Float(this.value + ((Number) other).value).setContext(this.context));
        }
        return super.add(other);
    }

    public OperationResult sub(Values other) {
        if (other instanceof Float) {
            return new OperationResult(new Float(this.value - ((Float) other).value).setContext(this.context));
        } else if (other instanceof Number) {
            return new OperationResult(new Float(this.value - ((Number) other).value).setContext(this.context));
        }
        return super.sub(other);
    }

    public OperationResult mul(Values other) {
        if (other instanceof Float) {
            return new OperationResult(new Float(this.value * ((Float) other).value).setContext(this.context));
        } else if (other instanceof Number) {
            return new OperationResult(new Float(this.value * ((Number) other).value).setContext(this.context));
        }
        return super.mul(other);
    }

    public OperationResult div(Values other) {
        if (other instanceof Float) {
            if (((Float) other).value.equals(0.0)) {
                return new OperationResult(new RunTimeError(this.pos_start, this.pos_end, "Division by zero", this.context));
            }
            return new OperationResult(new Float(this.value / ((Float) other).value).setContext(this.context));
        } else if (other instanceof Number) {
            if (((Number) other).value.equals(0)) {
                return new OperationResult(new RunTimeError(this.pos_start, this.pos_end, "Division by zero", this.context));
            }
            return new OperationResult(new Float(this.value / ((Number) other).value).setContext(this.context));
        }
        return super.div(other);
    }

    public OperationResult pow(Values other) {
        if (other instanceof Float) {
            return new OperationResult(new Float(Math.pow(this.value, ((Float) other).value)).setContext(this.context));
        } else if (other instanceof Number) {
            return new OperationResult(new Float(Math.pow(this.value, ((Number) other).value)).setContext(this.context));
        }
        return super.pow(other);
    }

    public OperationResult mod(Values other) {
        if (other instanceof Float) {
            if (((Float) other).value == 0) {
                return new OperationResult(new RunTimeError(this.pos_start, this.pos_end, "Division by zero", this.context));
            }
            return new OperationResult(new Float(this.value % ((Float) other).value).setContext(this.context));
        } else if (other instanceof Number) {
            if (((Number) other).value == 0) {
                return new OperationResult(new RunTimeError(this.pos_start, this.pos_end, "Division by zero", this.context));
            }
            return new OperationResult(new Float(this.value % ((Number) other).value).setContext(this.context));
        }
        return super.mod(other);
    }

    public OperationResult lt(Values other) {
        if (other instanceof Float) {
            return new OperationResult(new Number(this.value < ((Float) other).value ? 1 : 0).setContext(this.context));
        } else if (other instanceof Number) {
            return new OperationResult(new Number(this.value < ((Number) other).value ? 1 : 0).setContext(this.context));
        }
        return super.lt(other);
    }

    public OperationResult gt(Values other) {
        if (other instanceof Float) {
            return new OperationResult(new Number(this.value > ((Float) other).value ? 1 : 0).setContext(this.context));
        } else if (other instanceof Number) {
            return new OperationResult(new Number(this.value > ((Number) other).value ? 1 : 0).setContext(this.context));
        }
        return super.gt(other);
    }

    public OperationResult lte(Values other) {
        if (other instanceof Float) {
            return new OperationResult(new Number(this.value <= ((Float) other).value ? 1 : 0).setContext(this.context));
        } else if (other instanceof Number) {
            return new OperationResult(new Number(this.value <= ((Number) other).value ? 1 : 0).setContext(this.context));
        }
        return super.lte(other);
    }

    public OperationResult gte(Values other) {
        if (other instanceof Float) {
            return new OperationResult(new Number(this.value >= ((Float) other).value ? 1 : 0).setContext(this.context));
        } else if (other instanceof Number) {
            return new OperationResult(new Number(this.value >= ((Number) other).value ? 1 : 0).setContext(this.context));
        }
        return super.gte(other);
    }

    public boolean isTrue() {
        return this.value != 0;
    }
}