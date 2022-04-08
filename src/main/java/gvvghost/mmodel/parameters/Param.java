package gvvghost.mmodel.parameters;

public enum Param {
    TAU("Tau"),
    CURRENT("Current"),
    VELOCITY("Velocity"),
    PATH("Path"),
    BETA("Beta"),
    C1("C1"),
    C2("C2"),
    C3("C3"),
    ITERATION("Iteration"),
    UNDEFINED("Undefined");

    private final String name;

    Param(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}