package helper.widget;
import engine.GameEngine;
import helper.drawobjects.DrawObject;
import helper.enums.Callback;
import helper.enums.WidgetType;
import helper.function.FunctionMethods;
import helper.interfaces.IWidget;
import helper.io.IOHandler;
import helper.methods.PointInsideFunc;
import helper.struct.Vec2d;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static helper.enums.WidgetState.*;

public abstract class Widget implements IWidget{
    protected WidgetType wType;
    protected DrawObject wObj;
    public Widget next;
    public Widget childWidget; //id bind to other widget we want to access
    protected Object obj; //new FunctionMethods() initialized by GameEngine
    protected Object[] parameters; //ids to other widgets we want to update on different thread
    protected Method funcMethod;
    protected Callback callback;
    protected boolean runCallback;
    protected boolean orientated;
    protected boolean callbackInProgress;
    protected int widgetState;
    protected String wID;
    protected String childID;
    protected int shiftedBits;
    public Widget(Object obj,Callback callback,WidgetType type, DrawObject shape){
        this.obj = obj;
        this.callback = callback;
        this.next = null;
        this.wType = type;
        this.wObj = shape;
        this.setOrientated(false);
        this.widgetState = 0;
        this.callbackInProgress = false;
        if(callback != null){
            collectFuncMethod();
        }
        else{this.runCallback = false;}
   }

    public void collectFuncMethod(){
        try{
            Class[] parameterTypes = new Class[3];
            parameterTypes[0] = Integer.class;
            parameterTypes[1] = String[].class;
            parameterTypes[2] = Object.class;
            this.funcMethod = FunctionMethods.class.getMethod(this.callback.getValue(),parameterTypes);
            this.runCallback = true;
        }
        catch(NoSuchMethodException | SecurityException se){
            this.funcMethod = null;
            this.runCallback = false;
        }
    }
    public void setParameters(Object[] args){
        parameters = new Object[3];
        if(args != null){
            parameters[0] = args.length;
            parameters[1] = args;
            parameters[2] = this;
        }
        else{
            parameters[0] = 0;
            parameters[1] = null;
            parameters[2] = this;
        }
    }
    public void pointInside(int x,int y){
        if(PointInsideFunc.insideSelf(x,y,this.wObj)){setWidgetBit(SM_MOUSE_HOOVER.getValue());}
        else{clearWidgetBits();}
    }
    public boolean onFrameUpdate(int x,int y){return false;}
    public boolean onMouseLeftDown(int x,int y){return false;}
    public boolean onMouseLeftMove(int x,int y){return false;}
    public boolean onMouseLeftDoubleClick(int x,int y){return false;}
    public boolean onMouseRightDown(int x,int y){return false;}
    public boolean onMouseRightMove(int x,int y){return false;}
    public boolean onMouseRightDoubleClick(int x,int y){return false;}
    public boolean onMouseScrollUp(int x,int y){return false;}
    public boolean onMouseScrollDown(int x,int y){return false;}
    public boolean onMouseWheel(int x,int y){return false;}
    public boolean onMouseReleaseTouch(int x,int y){return false;}
    public boolean onKeyDown(){return false;}
    public boolean onKeyUp(){return false;}
    public boolean onText(){return false;}
    public boolean insideWidget(int x,int y){
        return insideWidgetWidth(x) && insideWidgetHeight(y);
    }
    public boolean insideWidgetWidth(int x){
        return x >= getPos().x && x <= getPos().x+getSize().x;
    }
    public boolean insideWidgetHeight(int y){
        return y >= getPos().y && y <= getPos().y+getSize().y;
    }
    public void setBindingValue(Object value){}
    public Object getBindingValue(){return null;}
    public String getID(){return wID;}
    public String getChildID(){return childID;}
    public void setChildID(String childid){childID = childid;}
    public void setID(String wid){wID = wid;}
    public void resetWidgetState(){}

    public void showWidgetBindToSelf(){
        int size = (int)parameters[0];
        if(size != 0){
            String[] wIDs = (String[]) parameters[1];
            for(int i = 0;i < size;i++){
                Widget w = GameEngine.getWidgetById(wIDs[i]);
                IOHandler.printString(w.wType.getValue());

            }
        }
    }

    public void setCallbackInProgress(boolean value){
        callbackInProgress = value;
    }

    public void execFuncMethod(){
        if(this.runCallback && !callbackInProgress){
            try{
                this.funcMethod.invoke(this.obj,parameters);
            }
            catch(IllegalAccessException | InvocationTargetException | IllegalArgumentException err){
                IOHandler.logToFile(err.getMessage());
            }
        }
    }

    public Vec2d getPos(){return this.wObj.getPos();}

    public Vec2d getSize(){return this.wObj.getSize();}

    public Vec2d getCenter(){return this.wObj.getCenter();}

    public boolean isOrientated(){return this.orientated;}

    public void setOrientated(boolean value){this.orientated = value;}

    public String getWidgetInfo(){
        return ("WidgetType: %s\n" +
                "DrawObject: %s\n" +
                "Center.x: %d\n" +
                "Center.y: %d\n" +
                "Pos.x: %d\n" +
                "Pos.y: %d\n" +
                "Size.x: %d\n" +
                "Size.y: %d\n" +
                "Color: %d\n").formatted(this.wType.getValue(),
                this.wObj.shape.getValue(),
                this.wObj.getCenter().x,
                this.wObj.getCenter().y,
                this.wObj.getPos().x,
                this.wObj.getPos().y,
                this.wObj.getSize().x,
                this.wObj.getSize().y,
                this.wObj.color);
    }

    public int getWidgetColorInfo(){
        return this.wObj.color;
    }

    protected void shiftBitsLeft(){
        shiftedBits = (wObj.color >> 24) & 0xff;
        wObj.color <<=8;
    }

    protected void shiftBitsRight(){
        wObj.color>>>=8;
        // wObj.color>>=8;
        // wObj.color &= ColorMask.OPACITY_MASK.getValue();
        wObj.color |= (shiftedBits)<<24;
    }

    protected boolean touchEventNotProcessed(){
        return ((widgetState & SM_TOUCH_PROCESSED.getValue()) == SM_MOUSE_HOOVER.getValue());
    }

    protected boolean widgetHasFocus(){
        return false;
    }

    protected void setWidgetBit(int value){
        widgetState |= value;
    }

    protected void clearWidgetBit(int pos){widgetState &= ~(1<<pos);}

    protected void clearWidgetBits(){
        widgetState = 0;
    }

    protected boolean getWidgetBitSet(int bitSet){
        return ((widgetState & (1 << bitSet)) != 0);
    }

}
