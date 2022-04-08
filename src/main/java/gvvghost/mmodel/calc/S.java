package gvvghost.mmodel.calc;

import gvvghost.mmodel.dataClasses.Callback;
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
    private static double currentDistanceTraveled;

    public static SimulationPointsMap simulateVCP() {
        SimulationPointsMap map = new SimulationPointsMap();
        currentDistanceTraveled = 0.0D;
        SimulationPoint spi = new SimulationPoint(0);
        spi.addValue(Param.CURRENT, iInit);
        spi.addValue(Param.VELOCITY, vInit);
        spi.addValue(Param.PATH, currentDistanceTraveled);
        map.addPoint(0, spi);
        for (int i = 1; i < (int) timeLimit / P.K; i++) {
            double velocity = Math.abs(C.calcVelct(
                    map.getPointValue(i - 1, Param.VELOCITY),
                    map.getPointValue(i - 1, Param.CURRENT)));
            double current = Math.abs(C.calcCurr(
                    map.getPointValue(i - 1, Param.CURRENT),
                    map.getPointValue(i - 1, Param.VELOCITY),
                    velocity, (int) i * 0.1D, c1, c2, c3));
            currentDistanceTraveled += velocity * P.K;
            SimulationPoint sp = new SimulationPoint(i);
            sp.addValue(Param.VELOCITY, velocity);
            sp.addValue(Param.CURRENT, current);
            sp.addValue(Param.PATH, currentDistanceTraveled);
            map.addPoint(i, sp);
        }
        return map;
    }

    public static void setAllParams(double c1, double c2, double c3, double beta, int dist, int time, double iInit, double vInit) {
        S.c1 = c1;
        S.c2 = c2;
        S.c3 = c3;
        S.beta = beta;
        S.timeLimit = time;
        S.distanceLimit = dist;
        S.vInit = vInit;
        S.iInit = iInit;
    }

    public static SimulationPointsMap simulateVCPB() {
        return simulateVCPB(null);
    }

    public static SimulationPointsMap simulateVCPB(Callback callback) {
        boolean callbackFlag = callback != null;
        SimulationPointsMap map = new SimulationPointsMap();
        currentDistanceTraveled = 0.0D;
        Param[] params = new Param[]{Param.CURRENT, Param.VELOCITY, Param.PATH, Param.BETA};
        map.addPoint(0, createSimulationPoint(0, params,
                new double[]{iInit, vInit, currentDistanceTraveled, beta}));
        for (int i = 1; i < (int) timeLimit / P.K; i++) {
            int movementOn = C.MOVING_ON_FLAT;
            if (beta > 0.0D) {
                movementOn = C.MOVING_ON_ASCENT;
            } else if (beta < 0.0D) {
                movementOn = C.MOVING_ON_DESCENT;
            }
            double C1 = c1;
            /*if (distanceLimit < currentDistanceTraveled) {
                if (callbackFlag) {
                    callback.callingBack(map.getPoint(i - 1));
                }
                callbackFlag = false;
                C1 = -1.0D;
            }*/
            double velocity = Math.abs(C.calcVelct(
                    map.getPointValue(i - 1, Param.VELOCITY),
                    map.getPointValue(i - 1, Param.CURRENT),
                    Math.abs(beta), movementOn));
            double current = Math.abs(C.calcCurr(
                    map.getPointValue(i - 1, Param.CURRENT),
                    map.getPointValue(i - 1, Param.VELOCITY),
                    velocity, (int) i * 0.1D, C1, c2, c3));
            currentDistanceTraveled += velocity * P.K;
            map.addPoint(i, createSimulationPoint(i, params,
                    new double[]{current, velocity, currentDistanceTraveled, beta}));
        }
        return map;
    }

    public static SimulationPointsMap simulateVSCB_AnD(SimulationPointsMap betaMap) {
        SimulationPointsMap map = new SimulationPointsMap();
        currentDistanceTraveled = 0.0D;
        double currBeta = 0.0D;
        Param[] params = new Param[]{Param.CURRENT, Param.VELOCITY, Param.PATH, Param.BETA};
        map.addPoint(0, createSimulationPoint(0, params,
                new double[]{iInit, vInit, currentDistanceTraveled, currBeta}));
        for (int i = 1; i < (int) timeLimit / P.K; i++) {
            int snb = (int) (C.roundAvoid(currentDistanceTraveled, 1) / P.K);
            try{
                currBeta = betaMap.getPointValue(snb, Param.BETA);
            } catch (NullPointerException e){
                currBeta = 0.0D;
            }

            int movementOn = C.MOVING_ON_FLAT;
            if (currBeta > 0.0D) {
                movementOn = C.MOVING_ON_ASCENT;
            } else if (currBeta < 0.0D) {
                movementOn = C.MOVING_ON_DESCENT;
            }
            double C1 = c1;
            double velocity = Math.abs(C.calcVelct(
                    map.getPointValue(i - 1, Param.VELOCITY),
                    map.getPointValue(i - 1, Param.CURRENT),
                    Math.abs(currBeta), movementOn)
            );
            double current = Math.abs(C.calcCurr(
                    map.getPointValue(i - 1, Param.CURRENT),
                    map.getPointValue(i - 1, Param.VELOCITY),
                    velocity, (int) i * 0.1D, C1, c2, c3)
            );
            currentDistanceTraveled += velocity * P.K;
            map.addPoint(i, createSimulationPoint(i, params,
                    new double[]{current, velocity, currentDistanceTraveled, currBeta}));
        }
        return map;
    }

    public static SimulationPointsMap simulateVSCB_AnDE(SimulationPointsMap betaMap){
        SimulationPointsMap map = new SimulationPointsMap();
        currentDistanceTraveled = 0.0D;
        double currBeta = 0.0D;
        Param[] params = new Param[]{Param.CURRENT, Param.VELOCITY, Param.PATH, Param.BETA};
        map.addPoint(0, createSimulationPoint(0, params,
                new double[]{P.I_INITIAL, P.V_INITIAL, currentDistanceTraveled, currBeta}));
        for (int i = 1; i < (int) timeLimit / P.K; i++) {

        }
        return map;
    }

    public static SimulationPointsMap createSimpleBetaMap(double beta, int distanceLimit, int timeLimit) {
        SimulationPointsMap betaMap = new SimulationPointsMap();
        timeLimit = (timeLimit + 10) * 10;
        distanceLimit *= 10;
        int sequenceNum = 0;
        double currBeta = 0;
        double step;
        double absBeta = Math.abs(beta);
        if (beta == 0) {
            for (; sequenceNum < distanceLimit; sequenceNum++) {
                betaMap.addPoint(sequenceNum, S.createSimulationPoint(sequenceNum, new Param[]{Param.BETA}, new double[]{currBeta}));
            }
        } else {
            if (beta > 0) {
                step = P.K;
            } else {
                step = -P.K;
            }
            for (; sequenceNum < 50; sequenceNum++) {
                betaMap.addPoint(sequenceNum, S.createSimulationPoint(sequenceNum, new Param[]{Param.BETA}, new double[]{currBeta}));
            }
            for (; sequenceNum < (50 + absBeta / P.K); sequenceNum++) {
                currBeta += step;
                betaMap.addPoint(sequenceNum, S.createSimulationPoint(sequenceNum, new Param[]{Param.BETA}, new double[]{C.roundAvoid(currBeta, 1)}));
            }
            currBeta = beta;
            for (; sequenceNum < (distanceLimit - absBeta / P.K); sequenceNum++) {
                betaMap.addPoint(sequenceNum, S.createSimulationPoint(sequenceNum, new Param[]{Param.BETA}, new double[]{C.roundAvoid(currBeta, 1)}));
            }
            for (; sequenceNum < (distanceLimit); sequenceNum++) {
                currBeta -= step;
                betaMap.addPoint(sequenceNum, S.createSimulationPoint(sequenceNum, new Param[]{Param.BETA}, new double[]{C.roundAvoid(currBeta, 1)}));
            }
        }
        currBeta = 0;
        if (timeLimit > distanceLimit) {
            for (; sequenceNum < timeLimit; sequenceNum++) {
                betaMap.addPoint(sequenceNum, S.createSimulationPoint(sequenceNum, new Param[]{Param.BETA}, new double[]{C.roundAvoid(currBeta, 1)}));
            }
        }
        return betaMap;
    }

    public static SimulationPoint createSimulationPoint(int sequenceNumber, Param[] params, double[] values) {
        SimulationPoint sp = new SimulationPoint(sequenceNumber);
        if (params.length == values.length) {
            for (int i = 0; i < params.length; i++) {
                sp.addValue(params[i], values[i]);
            }
        }
        return sp;
    }


}