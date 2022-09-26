package helper.enums;

public enum QuadGlobal {
    QUAD_ITEM_SIZE(10),
    QUAD_WIDTH(32001),
    QUAD_HEIGHT(32001),
    IL_FIXED_CAP(128);
    private final int value;
    QuadGlobal(int value){this.value = value;}
    public int getValue(){return this.value;}
}
