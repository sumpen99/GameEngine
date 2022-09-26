package helper.widget;

import helper.drawobjects.Ellipse;
import helper.enums.*;
import helper.struct.DrawValues;

public class RoundedButton extends Button{
    public RoundedButton(Object obj, DrawValues dww){
        super(obj,dww.functionMethod,WidgetType.SM_ROUNDED_BUTTON,
                new Ellipse(dww.left,dww.top,dww.radiex,dww.radiey,dww.color,dww.opacity,dww.draw),dww.text,dww.hintText,dww.textColor,dww.alignText);

    }
}
