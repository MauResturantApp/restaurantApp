package mau.resturantapp.typedefs;

public enum GraphType {
    DAY(0, "Daily"),
    WEEK(1, "Weekly"),
    FORTHNIGHT(2, "Forth night"),
    MONTH(3, "Monthly"),
    QUARTERLY(4, "Quarterly"),
    ANNUALLY(5, "Annually");

    private final int type;
    private final String name;

    GraphType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {return type;}
    public String toString() {return name;}

    public static GraphType isEnum(String s) {
        for(GraphType g : values())
            if(g.toString().equals(s)) return g;

        throw new IllegalArgumentException("No such GraphType");
    }
}
