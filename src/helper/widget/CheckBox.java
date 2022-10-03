package helper.widget;
import helper.drawobjects.Polygon;
import helper.drawobjects.Rectangle;
import helper.enums.Color;
import helper.enums.DrawMode;
import helper.enums.WidgetType;
import helper.struct.DrawValues;
import helper.struct.Point;
import helper.struct.Vec2d;

import static helper.methods.CommonMethods.buildPolygonShape;
import static helper.methods.CommonMethods.intArrToPointArr;

public class CheckBox extends Widget{
    boolean selected;
    Polygon checkerSymbol;
    public CheckBox(Object obj, DrawValues dww){
        super(obj,dww.functionMethod, WidgetType.SM_CHECK_BOX,
                new Rectangle(dww.left,dww.top,dww.width,dww.height,dww.color,dww.opacity,dww.draw));
        initPolygonSymbol(dww);
  }

    void initPolygonSymbol(DrawValues dww){
        int cx,cy,lx,ly,rx,ry;
        lx = dww.left+dww.width/3;
        ly = dww.top+dww.height/3;
        cx = dww.left+dww.width/2;
        cy = dww.top+dww.height/2;
        rx = dww.left+dww.width;
        ry = dww.top;
        checkerSymbol = new Polygon(new int[]{lx,ly,cx,cy,rx,ry}, Color.PALEGOLDENROD.getValue(),1, DrawMode.FILL);
    }

    @Override
    public Object getBindingValue(){
        return null;
    }

    @Override
    public void setBindingValue(Object value){

    }

    @Override
    public void reposition(Vec2d offset){
        this.wObj.rePosition(offset);
        checkerSymbol.rePosition(offset);
    }

    @Override
    public void draw(){
        if(this.wObj.opacity != 0){this.wObj.draw();}
        checkerSymbol.draw();
    }
}
