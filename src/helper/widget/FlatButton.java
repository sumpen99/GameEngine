package helper.widget;
import helper.drawobjects.Rectangle;
import helper.enums.*;
import helper.struct.DrawValues;

public class FlatButton extends Button{
    public FlatButton(Object obj, DrawValues dww){
        super(obj,dww.functionMethod,WidgetType.SM_FLAT_BUTTON,
                new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw),dww.text,dww.hintText,dww.textColor,dww.alignText);
    }
}
