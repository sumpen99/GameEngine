package helper.widget;

import helper.struct.DrawValues;

import static helper.enums.WidgetState.SM_TOUCH_PROCESSED;

public class Recorder extends FlatImage{
    public Recorder(Object obj, DrawValues dww){
        super(obj,dww);
    }

    @Override
    public boolean onMouseLeftDown(int x,int y){
        if(touchEventNotProcessed()){
            setWidgetBit(SM_TOUCH_PROCESSED.getValue());
            //shiftBitsLeft();
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseReleaseTouch(int x,int y){
        if(getWidgetBitSet(SM_TOUCH_PROCESSED.getIndex())){
            //shiftBitsRight();
            clearWidgetBits();
            execFuncMethod();
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseScrollUp(int x,int y){return false;}

    @Override
    public boolean onMouseScrollDown(int x,int y){return false;}


}
