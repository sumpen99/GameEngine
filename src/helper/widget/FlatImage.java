package helper.widget;
import helper.drawobjects.Rectangle;
import helper.enums.WidgetType;
import helper.struct.DrawValues;

public class FlatImage extends Image {
    public FlatImage(Object obj, DrawValues dww){
        super(obj,dww.functionMethod, WidgetType.SM_FLAT_IMAGE,
                new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw),dww.path);
    }

}
