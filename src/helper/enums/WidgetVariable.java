package helper.enums;

public enum WidgetVariable {
    IDENTITY("id"),
    BIND("bind"),
    FONT("font"),
    LEFT("left"),
    TOP("top"),
    WIDTH("width"),
    HEIGHT("height"),
    COL("col"),
    ROW("row"),
    ENABLE_AUTO_CORRECT("enableautocorrect"),
    TALIGN("talign"),
    HALIGN("halign"),
    VALIGN("valign"),
    CALLBACK("callback"),
    SELECTED("selected"),
    ARGS("args"),
    PATH("path"),
    OPACITY("opacity"),
    FONT_SIZE("fontsize"),
    TEXT("text"),
    HINT_TEXT("hinttext"),
    FUNCTION("function"),
    COLOR("color"),
    TEXT_COLOR("textcolor"),
    DRAW("draw"),
    POINTS("points"),
    OBJ_COUNT("objcount"),
    RADIE("radie"),
    RADIEX("radiex"),
    RADIEY("radiey"),
    DEGREES("degrees"),
    UPDATE("update"),
    WSHAPE("shape"),
    BLEND("blend"),
    WTYPE("type"),
    SM_VARIABLE_NOT_IMPLEMENTED("not a valid variable"),
    SM_VARIABLE_DUMMY("Dummy");
    private final String value;
    WidgetVariable(String value){this.value = value;}
    public String getValue(){return this.value;}
}
