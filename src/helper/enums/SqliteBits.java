package helper.enums;

public enum SqliteBits {
    MAGIC_STRING(16),           // 0 -> 16
    PAGE_SIZE(2),               // 16 -> 18
    FORMAT_VERSION_READ(1),     // 18 -> 19
    FORMAT_VERSION_WRITE(1),    // 19 -> 20
    RESERVED_BYTES(1),          // 20 -> 21
    FRACTION_MAX(1),            // 21 -> 22
    FRACTION_MIN(1),            // 22 -> 23
    FRACTION_LEAF(1),           // 23 -> 24
    CHANGE_COUNTER(4),          // 24 -> 28
    DATABASE_SIZE(4),           // 28 -> 32
    FIRST_FREE_PAGE_INFO(0),    //
    FIRST_FREE_PAGE(4),         // 32 -> 36
    FIRST_FREE_PAGE_LEN(4),     // 36 -> 40
    SCHEMA_COOKIE(4),           // 40 -> 44
    SCHEMA_VERSION(4),          // 44 -> 48
    CACHE_SIZE(4),              // 48 -> 52
    VACUUM_SETTING(4),          // 52 -> 56
    TEXT_ENCODING(4),           // 56 -> 60
    USER_VERSION(4),            // 60 -> 64
    INCREMENTAL_VACUUM(4),      // 64 -> 68
    APPLICATION_ID(4),          // 68 -> 72
    RESERVED_ZEROS(20),         // 72 -> 92
    VERSION_VALID_FOR(4),       // 92 -> 96
    LIBRARY_WRITE_VERSION(4),   // 96 -> 100
    MAX_MIN_LOCAL_LEAF(0);      // 0
    private final int value;
    SqliteBits(int value){this.value = value;}
    public int getValue(){return this.value;}
}
