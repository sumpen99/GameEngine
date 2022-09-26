package helper.enums;

public enum KeyboardState {
    SM_KEY_DOWN(0,1),
    SM_KEY_MULTI_TOUCH(1,2),
    SM_KEY_UP(2,4);

    private final int index;
    private final int value;
    KeyboardState(int index,int value){this.index = index;this.value = value;}
    public int getValue(){return this.value;}
    public int getIndex(){return this.index;}

}
