package helper.widget;
import helper.drawobjects.DrawObject;
import helper.enums.Callback;
import helper.enums.WidgetType;

public abstract class Image extends Sprite {
    public Image(Object obj, Callback callback, WidgetType type, DrawObject shape, String path){
        super(obj,callback,type,shape,path);
    }
}
