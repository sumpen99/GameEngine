package helper.enums;

public enum FormatVersion {
    LEGACY(1),
    WRITE_AHEAD_LOG(2),
    UNKNOWN(-1);

    private final int value;
    FormatVersion(int value){this.value = value;}
    public int getValue(){return this.value;}
}
