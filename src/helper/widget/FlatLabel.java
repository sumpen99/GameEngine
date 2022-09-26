package helper.widget;

import helper.drawobjects.Rectangle;
import helper.enums.WidgetType;
import helper.struct.DrawValues;

public class FlatLabel extends Label{
    public FlatLabel(Object obj, DrawValues dww){
        super(obj,dww.functionMethod,WidgetType.SM_FLAT_LABEL,
                new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw),dww.text,dww.hintText,dww.textColor,dww.alignText);
    }
}
