package helper.widget;
import helper.drawobjects.DrawObject;
import helper.enums.*;
import helper.io.IOHandler;
import helper.struct.Vec2d;
import static helper.enums.WidgetState.SM_TOUCH_PROCESSED;

public abstract class Button extends Label{
    public Button(Object obj,Callback callback,WidgetType type,DrawObject shape,String text,String hintText,int txtclr,boolean alignText){
        super(obj,callback,type,shape,text,hintText,txtclr,alignText);
    }

    @Override
    public boolean onMouseLeftDown(int x,int y){
        if(touchEventNotProcessed()){
            setWidgetBit(SM_TOUCH_PROCESSED.getValue());
            shiftBitsLeft();
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseReleaseTouch(int x,int y){
        if(getWidgetBitSet(SM_TOUCH_PROCESSED.getIndex())){
            shiftBitsRight();
            clearWidgetBits();
            execFuncMethod();
            return true;
        }
        return false;
    }

}
