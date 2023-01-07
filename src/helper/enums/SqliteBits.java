package helper.enums;

public enum SqliteBits {
    MAGIC_STRING(16),
    PAGE_SIZE(2);
    private final int value;
    SqliteBits(int value){this.value = value;}
    public int getValue(){return this.value;}
}
