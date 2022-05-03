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
}
