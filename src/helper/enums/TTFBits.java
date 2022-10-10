package helper.enums;

public enum TTFBits {
    SCALAR_TYPE(4),
    NUM_TABLES(2),
    SEARCH_RANGE(2),
    ENTRY_SELECTOR(2),
    RANGE_SHIFT(2);
    private final int value;
    TTFBits(int value){this.value = value;}
    public int getValue(){return this.value;}
}
