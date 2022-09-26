package helper.enums;

public enum ColorMask {
    OPACITY_MASK(0x00FFFFFF);
    private final int value;
    ColorMask(int value){this.value = value;}
    public int getValue(){return this.value;}
}
