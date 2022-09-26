package helper.enums;

public enum WidgetState {
    SM_MOUSE_HOOVER(0,1),
    SM_TOUCH_PROCESSED(1,3),
    SM_TEXT_HAS_FOCUS(2,4),
    SM_TEXT_KEEP_FOCUS(3,8);

    private final int index;
    private final int value;
    WidgetState(int index,int value){this.index = index;this.value = value;}
    public int getValue(){return this.value;}
    public int getIndex(){return this.index;}
}
