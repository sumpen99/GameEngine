package helper.enums;

public enum BTreePageType {
    INTERIOR_INDEX, // 0x02
    INTERIOR_TABLE, // 0x05
    LEAF_INDEX,     // 0x0a
    LEAF_TABLE,     // 0x0d
    UNKNOWN;
}
