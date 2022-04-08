package gvvghost.mmodel.dataClasses;

import gvvghost.mmodel.parameters.Param;

import java.util.ArrayList;

public interface Recordable {
    int getSequenceNumber();

    void addValue(Param name, double value);

    boolean hasValue(Param name);

    double getValue(Param name);

    ArrayList<Value> getValues();
}
