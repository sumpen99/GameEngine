package helper.enums;
public enum MouseState {
    SM_MOUSE_LEFT_DOWN(0,1),
    SM_MOUSE_LEFT_MOVE(1,3),
    SM_MOUSE_RIGHT_DOWN(2,4),
    SM_MOUSE_RIGHT_MOVE(3,12),
    SM_MOUSE_LEFT_DOUBLE_CLICK(4,16),
    SM_MOUSE_RIGHT_DOUBLE_CLICK(5,32),
    SM_MOUSE_TOUCH_UP(6,64),
    SM_MOUSE_TOUCH_MOVE(7,128),
    SM_MOUSE_RELEASE_TOUCH(8,256),
    SM_MOUSE_SCROLL_UP(9,512),
    SM_MOUSE_SCROLL_DOWN(10,1024),
    SM_MOUSE_WHEEL(11,2048);

    private final int value;
    private final int index;
    MouseState(int index,int value){this.index=index;this.value = value;}
    public int getValue(){return this.value;}
    public int getIndex(){return this.index;}
}
