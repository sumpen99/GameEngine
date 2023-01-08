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
    DATABASE_SIZE(4),
    FIRST_FREE_PAGE_INFO(0),
    FIRST_FREE_PAGE(4),
    FIRST_FREE_PAGE_LEN(4),
    SCHEMA_COOKIE(4),
    SCHEMA_VERSION(4),
    CACHE_SIZE(4),
    VACUUM_SETTING(4),
    TEXT_ENCODING(4),
    USER_VERSION(4),
    INCREMENTAL_VACUUM(4),
    APPLICATION_ID(4),
    RESERVED_ZEROS(20),
    VERSION_VALID_FOR(4),
    LIBRARY_WRITE_VERSION(4);
    private final int value;
    SqliteBits(int value){this.value = value;}
    public int getValue(){return this.value;}
}
