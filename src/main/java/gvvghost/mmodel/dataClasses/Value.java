package gvvghost.mmodel.dataClasses;

import gvvghost.mmodel.parameters.Param;

import java.util.Objects;

public class Value {
    private final Param param;
    private final double value;

    public Value(Param param, double value) {
        this.param = param;
        this.value = value;
    }

    public Param getParam() {
        return param;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        return Double.compare(value1.value, value) == 0 && param == value1.param;
    }

    @Override
    public int hashCode() {
        return Objects.hash(param, value);
    }
}
