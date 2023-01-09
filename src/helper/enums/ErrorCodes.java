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
    DATABASE_SIZE_MISMATCH("Database size out of bounds"),
    FIRST_FREE_PAGE_INFO_MISMATCH("First free page info not set up correspondly"),
    FIRST_FREE_PAGE_MISMATCH("First free page out of bounds"),
    FIRST_FREE_PAGE_LENGTH_MISMATCH("First free page length out of bounds"),
    SCHEMA_COOKIE_MISMATCH("Schema cookie out of bounds"),
    SCHEMA_VERSION_MISMATCH("Schema version out of bounds (valid be range 1-4)"),
    CACHE_SIZE_MISMATCH("Cache size out of bounds"),
    VACUUM_SETTINGS_RAW_MISMATCH("Vacuum settings raw most be in range 0 -> 4,294,967,295"),
    VACUUM_SETTINGS_MISMATCH("Vacuum settings most be in range 0 -> 4,294,967,295"),
    TEXT_ENCODING_MISMATCH("Text encoding must be in range of 1-3"),
    USER_VERSION_MISMATCH("User version most be in range 0 -> 4,294,967,295"),
    IS_INCREMENTAL_MISMATCH("Is incremental most be in range 0 -> 4,294,967,295"),
    APPLICATION_ID_MISMATCH("Application ID out of bounds"),
    RESERVED_ZEROS_MISMATCH("Reserved zeros (20b) needs to be zero"),
    VERSION_VALID_FOR_MISMATCH("Version valid for out of bounds"),
    LIBRARY_WRITE_VERSION_MISMATCH("Library write version for out of bounds"),
    PAGE_TYPE_MISMATCH("Page type can only be -> (2, 5, 10 or 13)"),
    PAGE_NUMBER_MISMATCH("Page Number can not go outside the range of uint32");

    private final String value;
    ErrorCodes(String value){this.value = value;}
    public String getMessage(){return this.value;}
}
