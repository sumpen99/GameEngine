package helper.enums;

public enum WidgetShape{
    SM_RECTANGLE("rectangle"),
    SM_CIRCLE("circle"),
    SM_ELLIPSE("ellipse"),
    SM_POLYGON("polygon"),
    SM_TRIANGLE("triangle"),
    SM_LINE("line"),
    SM_WIDGET_SHAPE_NOT_IMPLEMENTED("Not a valid shape");

    private final String value;
    WidgetShape(String value){this.value = value;}
    public String getValue(){return this.value;}
}
