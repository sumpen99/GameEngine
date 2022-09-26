package helper.enums;

public enum KeyType {
    KEY_ENTER(10),
    KEY_DUMMY(0),
    KEY_LEFT(37),
    KEY_UP(38),
    KEY_RIGHT(39),
    KEY_DOWN(40),
    KEY_BACKSPACE(8);

    private final int value;
    KeyType(int value){this.value = value;}
    public int getValue(){return this.value;}

}
