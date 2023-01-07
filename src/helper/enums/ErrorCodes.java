package helper.enums;

public enum ErrorCodes {
    MAGIC_NUMBER_MISMATCH("Magic String Is Not A Match"),
    PAGE_SIZE_TO_SMALL("PageSize can not be less then 512"),
    PAGE_SIZE_TO_BIG("PageSize can not be greater then 65536"),
    PAGE_SIZE_NOT_A_POWER_OF_TWO("PageSize must be a power of 2"),
    FORMAT_VERSION_READ_UNKNOWN("Format version read is not reconized"),
    FORMAT_VERSION_WRITE_UNKNOWN("Format version write is not reconized"),
    RESERVED_BYTES_MISMATCH("Reserved bytes can not be less then 0"),
    FRACTION_MAX_MISMATH("Maximum payload fraction out of bounds"),
    FRACTION_MIN_MISMATH("Minimum payload fraction out of bounds"),
    FRACTION_LEAF_MISMATH("Leaf fraction out of bounds"),
    CHANGE_COUNTER_MISMATH("Change counter out of bounds"),
    DATABASE_SIZE_MISMATCH("Database size out of bounds");

    private final String value;
    ErrorCodes(String value){this.value = value;}
    public String getMessage(){return this.value;}
}
