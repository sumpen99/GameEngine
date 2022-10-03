package helper.widget;
import helper.drawobjects.Polygon;
import helper.drawobjects.Rectangle;
import helper.enums.Color;
import helper.enums.DrawMode;
import helper.enums.WidgetType;
import helper.struct.DrawValues;
import helper.struct.Point;
import helper.struct.Vec2d;

import static helper.enums.WidgetState.SM_TOUCH_PROCESSED;
import static helper.methods.CommonMethods.buildPolygonShape;
import static helper.methods.CommonMethods.intArrToPointArr;

public class CheckBox extends Widget{
    boolean selected;
    Polygon checkerSymbol;
    public CheckBox(Object obj, DrawValues dww){
        super(obj,dww.functionMethod, WidgetType.SM_CHECK_BOX,
                new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw));
        initPolygonSymbol(dww);
        selected = true;
  }

    void initPolygonSymbol(DrawValues dww){
        int cx,cy,lx,ly,rx,ry;
        lx = dww.left+dww.width/4;
        ly = dww.top+dww.height/4;
        cx = dww.left+dww.width/2;
        cy = dww.top+dww.height/2;
        rx = dww.left+dww.width;
        ry = dww.top;
        checkerSymbol = new Polygon(new int[]{lx,ly,cx,cy,rx,ry},dww.width/10,Color.LIGHTGREEN.getValue(),1, DrawMode.FILL);
    }

    @Override
    public Object getBindingValue(){
        return null;
    }

    @Override
    public void setBindingValue(Object value){

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
            selected ^= true;
            return true;
        }
        return false;
    }

    @Override
    public void reposition(Vec2d offset){
        this.wObj.rePosition(offset);
        checkerSymbol.rePosition(offset);
    }

    @Override
    public void draw(){
        if(this.wObj.opacity != 0){this.wObj.draw();}
        if(selected)checkerSymbol.draw();
    }
}
