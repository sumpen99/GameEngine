package helper.enums;

public enum ColorMask {
    RED_MASK(0XFF000000),
    GREEN_MASK(0X00FF0000),
    BLUE_MASK(0X0000FF00),
    ALPHA_MASK(0X000000FF),
    OPACITY_MASK(0x00FFFFFF);
    private final int value;
    ColorMask(int value){this.value = value;}
    public int getValue(){return this.value;}
}
