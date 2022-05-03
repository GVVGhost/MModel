package gvvghost.mmodel.calc;

import gvvghost.mmodel.parameters.P;

public class C {
    public static final int HORIZONTALLY = 0;
    public static final int UPHILL = 1;
    public static final int DOWNHILL = 2;

    public static double calcCurr(double ipr, double vpr, double v,
                                  double tau, double c1, double c2, double c3) {
        double current = 0.0D;
        for (int n = 1; n < 500; n++) {
            current = ipr - (c3 / n) * (P._2A * Math.sqrt(Math.pow(ipr, 2.0D) + 1) * (1 - P._2ALPHA * ipr) -
                    ((P.A_POW2 + P.B_POW2) / P.B) * (c1 * Math.exp(P.F1 * tau + P._2F2 * vpr)) - (c2 / (P.F1 + P._2F2 * v)));
            if (Math.abs(current - ipr) <= P.EPSILON) break;
        }
        return (current < 0.01D) ? 0 : round(current, 5);
    }

    public static double calcVel(double vpr, double ipr, double beta, int move) {
        double f = P.F0;
        beta = Math.toRadians(beta);
        if (move == DOWNHILL) f = -P.F0 * Math.cos(beta) + P.F01 * Math.sin(beta);
        if (move == UPHILL) f = -P.F0 * Math.cos(beta) - P.F01 * Math.sin(beta);
        double velocity = vpr + ((P.A_POW2 + P.B_POW2) / P._2AB) * P.K * Math.log(ipr + Math.sqrt(Math.pow(ipr, 2.0D) + 1)) -
                (f + P.F1 * vpr + P.F2 * Math.pow(vpr, 2.0D)) * P.K;
        return (velocity <= 0.01) ? 0 : round(velocity, 5);
    }

    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
