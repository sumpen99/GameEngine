package helper.widget;
import engine.GameEngine;
import helper.struct.DrawValues;
import helper.struct.Vec2d;

import static helper.enums.WidgetState.SM_TOUCH_PROCESSED;

public class MoveableButton extends FlatButton{
    public MoveableButton(Object obj, DrawValues dww){
        super(obj,dww);
        startPos = new Vec2d();
        mPos = new Vec2d();
    }

    @Override
    public void shiftBitsLeft(){
        shiftedBits = (txtColor >> 24) & 0xff;
        txtColor <<=8;
    }

    @Override
    public void shiftBitsRight(){
        txtColor>>>=8;
        txtColor |= (shiftedBits)<<24;
    }

    @Override
    public boolean onMouseLeftDown(int x,int y){
        if(touchEventNotProcessed()){
            startPos.x = x;
            startPos.y = y;
            GameEngine.setMoveWindow(true);
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
            GameEngine.setMoveWindow(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseLeftMove(int x,int y){
        if(getWidgetBitSet(SM_TOUCH_PROCESSED.getIndex())){
            mPos.x = x-startPos.x;
            mPos.y = y-startPos.y;
            GameEngine.setNewWindowPos(mPos.x,mPos.y);
            startPos.x = x;
            startPos.y = y;
            return true;
        }
        return false;
    }

}
