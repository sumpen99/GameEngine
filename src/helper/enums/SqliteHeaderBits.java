package helper.enums;

public enum SqliteHeaderBits {
    PAGE_TYPE(1),                       // 0 -> 1
    START_FRIST_FREE_BLOCK(2),          // 1 -> 3
    NUMBER_OF_CELLS(2),                 // 3 -> 5
    START_OF_CELL_CONTENT_AREA(2),      // 5 -> 7
    NUMBER_OF_FRAGMENTED_FREE_BYTES(1), // 7 -> 8
    RIGHT_POINTER(4);                     // 8 -> 12
    private final int value;
    SqliteHeaderBits(int value){this.value = value;}
    public int getValue(){return this.value;}
}
