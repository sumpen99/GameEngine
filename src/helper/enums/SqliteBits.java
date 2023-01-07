package helper.enums;

public enum SqliteBits {
    MAGIC_STRING(16),
    PAGE_SIZE(2),
    FORMAT_VERSION_READ(1),
    FORMAT_VERSION_WRITE(1);
    private final int value;
    SqliteBits(int value){this.value = value;}
    public int getValue(){return this.value;}
}
