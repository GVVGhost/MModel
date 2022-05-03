package gvvghost.mmodel.parameters;

public class P {
    // calculated parameters of the model
    public static final double A         = 50.0D;
    public static final double B         = 5.898D;
    public static final double ALPHA     = 0.235D;
    public static final double EPSILON   = 0.001D;
    public static final double F0        = 0.04D;
    public static final double F01       = 2.13D;
    public static final double F1        = 0.009038D;
    public static final double F2        = 0.542D;
    // time step
    public static final double K         = 0.1D;
    // initial values
    public static final double I_INITIAL = 1.0D; // current
    public static final double V_INITIAL = 0.0D; // velocity
    // default regulating coefficients
    public static final double C1        = -0.0D;
    public static final double C2        = -0.001307D;
    public static final double C3        = 0.01838D;
    // default boundary conditions for vehicle motion simulation
    public static final int    distDef   = 60;
    public static final int    timeDef   = 50;
    // default road slope angle
    public static final int    betaDef   = 0;
    //additional parameters that do not change in the calculations
    public static final double _2A       = 2 * A;
    public static final double _2F2      = 2 * F2;
    public static final double _2ALPHA   = 2 * ALPHA;
    public static final double A_POW2    = Math.pow(P.A, 2.0D);
    public static final double B_POW2    = Math.pow(P.B, 2.0D);
    public static final double _2AB      = 2 * A * B;
}
