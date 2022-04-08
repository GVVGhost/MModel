package gvvghost.mmodel.calc;

import gvvghost.mmodel.parameters.P;

public class C {
    public static final int MOVING_ON_FLAT = 0;
    public static final int MOVING_ON_ASCENT = 1;
    public static final int MOVING_ON_DESCENT = 2;

    public static double calcCurr(double ipr,
                                  double vpr,
                                  double v,
                                  double tau,
                                  double c1,
                                  double c2,
                                  double c3) {
        double current = 0.0D;
        for (int n = 1; n < 500; n++) {
            current = ipr - (c3 / n) * (2 * P.A * Math.sqrt(Math.pow(ipr, 2.0D) + 1) * (1 - 2 * P.ALPHA * ipr) - ((Math.pow(P.A, 2.0D) + Math.pow(P.B, 2.0D)) / P.B) * (c1 * Math.exp(P.F1 * tau + 2 * P.F2 * vpr)) - (c2 / (P.F1 + 2 * P.F2 * v)));
            if (Math.abs(current - ipr) <= P.EPSILON) {
                break;
            }
        }
        if (current < 0.01D) {
            return 0;
        }
        return roundAvoid(current, 5);
    }

    public static double calcVelct(double vpr,
                                   double ipr) {
        return calcVelct(vpr, ipr, 0.0D, MOVING_ON_FLAT);
    }

    public static double calcVelct(double vpr,
                                   double ipr,
                                   double beta,
                                   int movementOn) {
        double f = P.F0;
        beta = Math.toRadians(beta);
        switch (movementOn) {
            case MOVING_ON_DESCENT:
                f = -P.F0 * Math.cos(beta) + P.F01 * Math.sin(beta);
                break;
            case MOVING_ON_ASCENT:
                f = -P.F0 * Math.cos(beta) - P.F01 * Math.sin(beta);
                break;
        }

        /*
        vpr + ((Math.pow(P.A, 2.0D) + Math.pow(P.B, 2.0D)) / (2 * P.A * P.B)) * P.K * Math.log(ipr + Math.sqrt(Math.pow(ipr, 2.0D) + 1)) - (f + P.F1 * vpr + P.F2 * Math.pow(vpr, 2.0D)) * P.K;
        vpr +
              ((Math.pow(P.A, 2.0D) + Math.pow(P.B, 2.0D)) / (2 * P.A * P.B)) *
                                                                                P.K *
                                                                                      Math.log(ipr + Math.sqrt(Math.pow(ipr, 2.0D) + 1)) -
                                                                                                                                           (f + P.F1 * vpr + P.F2 * Math.pow(vpr, 2.0D)) * P.K
        */
        double velocity = vpr + ((Math.pow(P.A, 2.0D) + Math.pow(P.B, 2.0D)) / (2 * P.A * P.B)) * P.K * Math.log(ipr + Math.sqrt(Math.pow(ipr, 2.0D) + 1)) - (f + P.F1 * vpr + P.F2 * Math.pow(vpr, 2.0D)) * P.K;

        if (velocity <= 0.01) {
            return 0;
        }
        return roundAvoid(velocity, 5);
    }

//    public static double recalcC1(double i, double v, double c2) {
//        return ((2 * P.A * P.B) / (Math.pow(P.A, 2.0D) + Math.pow(P.B, 2.0D))) * (1 - 2 * P.ALPHA * i) + (c2 / (P.F1 + 2 * P.F2 * v));
//    }
//
//    public static double recalcC2(double i0, double il, double v0, double vl, double tl, double l) {
//        return (2 * P.A * P.B * (P.F1 + 2 * P.F2 * v0) * (P.F1 + 2 * P.F2 * vl) * calcMFlux(i0, il, tl, l)) / ((Math.pow(P.A, 2.0D) + Math.pow(P.B, 2.0D)) * ((P.F1 + 2 * P.F2 * vl) * Math.exp(P.F1 * tl + 2 * P.F2 * l) - P.F1 - 2 * P.F2 * v0));
//    }
//
//    public static double calcMFlux(double i0, double il, double tl, double l) {
//        return Math.sqrt(Math.pow(il, 2.0D) + 1) * (1 - 2 * P.ALPHA * il) - (1 - 2 * P.ALPHA * i0) * Math.exp(P.F1 * tl + 2 * P.F2 * l);
//    }
//
//    public static double calc_dvl(double dvl, double vl) {
//        double il = calcil(dvl, vl);
//        return 0.0D;
//    }
//
//    private static double calcil(double dvl, double vl) {
//        double vsum = calc_vsum(dvl, vl);
//        return (Math.pow(Math.E, 2.0D * vsum)) / (2 * Math.pow(Math.E, vsum));
//    }
//
//    public static double calc_vsum(double dvl, double vl) {
//        return ((2 * P.A * P.B) / (Math.pow(P.A, 2.0D) + Math.pow(P.B, 2.0D))) * (dvl + P.F0 + P.F1 * vl + P.F2 * Math.pow(vl, 2.0D));
//    }

    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
