package helper.enums;
//UINT16_MAX(65535),
//UINT32_MAX(4,294,967,295),
//UINT64_MAX(18,446,744,073,709,551,615)
//INT16_MAX(32767),
//INT32_MAX(2,147,483,647),
//INT64_MAX(9,223,372,036,854,775,807),
//INT16_MIN(-32767),
//INT32_MIN(-2,147,483,647),
//INT64_MIN(-9,223,372,036,854,775,807),
//LONG_MAX_VALUE(9223372036854775807),
//LONG_MIN_VALUE(-9223372036854775807)
public enum ConstantValues {
    SINGLE_BYTE(1),
    KILO_BYTE(1024),
    MEGA_BYTE(1048576),
    GIGA_BYTE(1073741824),
    TERA_BYTE(1099511627776L);
    private final long value;
    ConstantValues(long value){this.value = value;}
    public long getValue(){return this.value;}
}
