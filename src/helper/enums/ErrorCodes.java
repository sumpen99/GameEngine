package helper.enums;

public enum ErrorCodes {
    MAGIC_NUMBER_MISMATCH("Magic String Is Not A Match"),
    PAGE_SIZE_TO_SMALL("PageSize can not be less then 512"),
    PAGE_SIZE_TO_BIG("PageSize can not be greater then 65536"),
    PAGE_SIZE_NOT_A_POWER_OF_TWO("PageSize must be a power of 2");

    private final String value;
    ErrorCodes(String value){this.value = value;}
    public String getMessage(){return this.value;}
}
