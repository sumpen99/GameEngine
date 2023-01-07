package helper.enums;

public enum SqliteBits {
    MAGIC_STRING(16),
    PAGE_SIZE(2),
    FORMAT_VERSION_READ(1),
    FORMAT_VERSION_WRITE(1),
    RESERVED_BYTES(1),
    FRACTION_MAX(1),
    FRACTION_MIN(1),
    FRACTION_LEAF(1),
    CHANGE_COUNTER(4),
    DATABASE_SIZE(4);
    private final int value;
    SqliteBits(int value){this.value = value;}
    public int getValue(){return this.value;}
}
