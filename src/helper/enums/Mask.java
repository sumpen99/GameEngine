package helper.enums;

public enum Mask
{
    DEG_45(0x01),
    DEG_90(0x03),
    DEG_135(0x07),
    DEG_180(0x0F),
    DEG_225(0x1F),
    DEG_270(0x3F),
    DEG_315(0x7F),
    DEG_360(0xFF),
    DEG_1_45(0x01),
    DEG_2_45(0x02),
    DEG_3_45(0x04),
    DEG_4_45(0x08),
    DEG_5_45(0x10),
    DEG_6_45(0x20),
    DEG_7_45(0x40),
    DEG_8_45(0x80);

    public final int bit;
    Mask(int f) {this.bit = f;}

}