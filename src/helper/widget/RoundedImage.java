package helper.widget;
import helper.drawobjects.Ellipse;
import helper.enums.WidgetType;
import helper.struct.DrawValues;

public class RoundedImage extends Image{
    public RoundedImage(Object obj, DrawValues dww){
        super(obj,dww.functionMethod, WidgetType.SM_ROUNDED_IMAGE,
                new Ellipse(dww.left,dww.top,dww.width/2,dww.height/2,dww.color,dww.opacity,dww.draw),dww.path);

    }
}
