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

        //checkerSymbol = new Polygon(dww.points, Color.PALEGOLDENROD.getValue(),1, DrawMode.FILL);
        //Point[] points = intArrToPointArr(dww.points);
        //points = buildPolygonShape(points);
        //int[] verts,int color, int opacity, DrawMode draw
        //int[] points = buildPolygonShape(new int[]{0,0,50,50,100,90});
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
    }

    @Override
    public void draw(){
        if(this.wObj.opacity != 0){this.wObj.draw();}
        // draw Checker
    }
}
