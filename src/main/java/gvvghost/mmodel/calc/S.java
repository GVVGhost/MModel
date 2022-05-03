package gvvghost.mmodel.calc;

import gvvghost.mmodel.dataClasses.SimulationPoint;
import gvvghost.mmodel.dataClasses.SimulationPointsMap;
import gvvghost.mmodel.parameters.P;
import gvvghost.mmodel.parameters.Param;

public class S {
    public static int distanceLimit = 50;
    public static int timeLimit = 50;
    public static double c1 = P.C1;
    public static double c2 = P.C2;
    public static double c3 = P.C3;
    public static double beta = 0;
    public static double iInit = P.I_INITIAL;
    public static double vInit = P.V_INITIAL;
    private static final Param[] paramsCVPB = new Param[]{Param.CURRENT, Param.VELOCITY, Param.PATH, Param.BETA};

    public static SimulationPointsMap simulateVCPB() {
        SimulationPointsMap map = new SimulationPointsMap();
        double dist = 0.0D;
        map.addPoint(0, createSP(0, paramsCVPB, new double[]{iInit, vInit, dist, beta}));
        for (int i = 1; i < (int) (timeLimit / P.K); i++) {
            int movementOn = C.HORIZONTALLY;
            if (beta > 0.0D)  movementOn = C.UPHILL;
            else if (beta < 0.0D) movementOn = C.DOWNHILL;
            double vpr = map.getPointValue(i - 1, Param.VELOCITY);
            double ipr = map.getPointValue(i - 1, Param.CURRENT);
            double velocity = Math.abs(C.calcVel(vpr, ipr, Math.abs(beta), movementOn));
            double current = Math.abs(C.calcCurr(ipr, vpr, velocity, (int) (i * P.K), c1, c2, c3));
            dist += velocity * P.K;
            map.addPoint(i, createSP(i, paramsCVPB, new double[]{current, velocity, dist, beta}));
        }
        return map;
    }

    public static SimulationPointsMap simulateVSCB_AnD(SimulationPointsMap betaMap) {
        SimulationPointsMap map = new SimulationPointsMap();
        double dist = 0.0D;
        double currBeta = (betaMap == null) ? beta : 0.0D;
        map.addPoint(0, createSP(0, paramsCVPB, new double[]{iInit, vInit, dist, currBeta}));
        for (int i = 1; i < (int) (timeLimit / P.K); i++) {
            int snb = (int) (C.round(dist, 1) / P.K);
            try{
                currBeta = betaMap.getPointValue(snb, Param.BETA);
            } catch (NullPointerException e){
                currBeta = 0.0D;
            }
            int movementOn = C.HORIZONTALLY;
            if (currBeta > 0.0D) movementOn = C.UPHILL;
            else if (currBeta < 0.0D) movementOn = C.DOWNHILL;
            double vpr = map.getPointValue(i - 1, Param.VELOCITY);
            double ipr = map.getPointValue(i - 1, Param.CURRENT);
            double v = Math.abs(C.calcVel(vpr, ipr, Math.abs(currBeta), movementOn));
            double c = Math.abs(C.calcCurr(ipr, vpr, v, (int) (i * P.K), c1, c2, c3));
            dist += v * P.K;
            map.addPoint(i, createSP(i, paramsCVPB, new double[]{c, v, dist, currBeta}));
        }
        return map;
    }

    public static SimulationPointsMap createSimpleBetaMap(double beta, int distLimit, int timeLimit) {
        SimulationPointsMap betaMap = new SimulationPointsMap();
        int m = (int) (1 / P.K);
        timeLimit = (timeLimit + m) * m;
        distLimit *= m;
        int seqNum = 0;
        double currBeta = 0.0D;
        double step;
        double absBeta = Math.abs(beta);
        if (beta == 0) for (; seqNum < distLimit; seqNum++) betaMap.addPoint(seqNum, S.createSP(seqNum, Param.BETA, currBeta));
        else {
            if (beta > 0) step = P.K;
            else step = -P.K;
            for (; seqNum < 50; seqNum++) betaMap.addPoint(seqNum, S.createSP(seqNum, Param.BETA, currBeta));
            for (; seqNum < (50 + (int)(absBeta / P.K)); seqNum++) {
                currBeta += step;
                betaMap.addPoint(seqNum, S.createSP(seqNum, Param.BETA, C.round(currBeta, 1)));
            }
            currBeta = beta;
            for (; seqNum < (distLimit - (int)(absBeta / P.K)); seqNum++)
                betaMap.addPoint(seqNum, S.createSP(seqNum, Param.BETA, C.round(currBeta, 1)));
            for (; seqNum < distLimit; seqNum++) {
                currBeta -= step;
                betaMap.addPoint(seqNum, S.createSP(seqNum, Param.BETA, C.round(currBeta, 1)));
            }
        }
        currBeta = 0.0D;
        if (timeLimit > distLimit)
            for (; seqNum < timeLimit; seqNum++)
                betaMap.addPoint(seqNum, S.createSP(seqNum, Param.BETA, C.round(currBeta, 1)));
        return betaMap;
    }

    public static SimulationPoint createSP(int sequenceNumber, Param param, double value){
        SimulationPoint sp = new SimulationPoint(sequenceNumber);
        sp.addValue(param, value);
        return sp;
    }

    public static SimulationPoint createSP(int sequenceNumber, Param[] params, double[] values) {
        if (params.length != values.length)
            throw new IllegalArgumentException("The size of the argument arrays is not the same");
        SimulationPoint sp = new SimulationPoint(sequenceNumber);
        for (int i = 0; i < params.length; i++) sp.addValue(params[i], values[i]);
        return sp;
    }
}