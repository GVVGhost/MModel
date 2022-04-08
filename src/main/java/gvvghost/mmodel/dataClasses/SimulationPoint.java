package gvvghost.mmodel.dataClasses;

import gvvghost.mmodel.parameters.Param;

import java.util.ArrayList;

public class SimulationPoint implements Recordable {
    private final int sequenceNumber;
    private ArrayList<Value> myListOfValues;

    public SimulationPoint(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        this.myListOfValues = new ArrayList<>();
    }

    @Override
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public void addValue(Param name, double value) {
        myListOfValues.add(new Value(name, value));
    }

    @Override
    public boolean hasValue(Param name) {
        return myListOfValues.contains(name);
    }

    @Override
    public double getValue(Param name) {
        for (Value v : myListOfValues) {
            if (v.getParam().equals(name)) {
                return v.getValue();
            }
        }
        return 0.0D;
    }

    @Override
    public ArrayList<Value> getValues() {
        return myListOfValues;
    }
}