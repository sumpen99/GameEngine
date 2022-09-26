package helper.enums;

public enum DrawMode{
    FILL("fill"),
    OUTLINE("outlined"),
    DOT("dot"),
    TRANSPARENT("transparent"),
    SM_DRAWMODE_NOT_IMPLEMENTED("not a valid drawmode");
    private final String value;
    DrawMode(String value){this.value = value;}
    public String getValue(){return this.value;}
}
