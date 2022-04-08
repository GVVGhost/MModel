package gvvghost.mmodel.dataClasses;

import gvvghost.mmodel.parameters.Param;

import java.util.HashMap;

public class SimulationPointsMap {
    private HashMap<Integer, SimulationPoint> mySimulationMap;

    public SimulationPointsMap() {
        this.mySimulationMap = new HashMap<>();
    }

    public void addPoint(int sequenceNumber, SimulationPoint point) {
        mySimulationMap.put(sequenceNumber, point);
    }

    public int getMapSize() {
        return mySimulationMap.size();
    }

    public SimulationPoint getPoint(int sequenceNumber) {
        return mySimulationMap.get(sequenceNumber);
    }

    public double getPointValue(int sequenceNumber, Param value) {
        if (sequenceNumber > getMapSize()){
            return 0.0D;
        }
        return mySimulationMap.get(sequenceNumber).getValue(value);
    }

    public HashMap<Integer, SimulationPoint> getMap() {
        return mySimulationMap;
    }

    public void clearMap() {
        mySimulationMap.clear();
    }
}
