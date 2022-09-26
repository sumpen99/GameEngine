package helper.widget;
import helper.drawobjects.Ellipse;
import helper.enums.WidgetType;
import helper.struct.DrawValues;

public class RoundedLabel extends Label{
    public RoundedLabel(Object obj, DrawValues dww){
        super(obj,dww.functionMethod,WidgetType.SM_ROUNDED_LABEL,
                new Ellipse(dww.left,dww.top,dww.radiex,dww.radiey,dww.color,dww.opacity,dww.draw),dww.text,dww.hintText,dww.textColor,dww.alignText);
    }
}
