package helper.methods;
import java.lang.Math;
import helper.drawobjects.Circle;
import helper.drawobjects.DrawObject;
import helper.enums.WidgetShape;
import helper.drawobjects.DrawObject;
import helper.struct.Vec2d;

public class PointInsideFunc {
    public static boolean insideSelf(int x, int y, DrawObject obj){
        switch(obj.shape){
            case SM_CIRCLE -> {
                return pointInsideCircle(x,y,(int[])obj.getValues());
            }
            case SM_TRIANGLE -> {
                return pointInsideTriangle(x,y,(int[])obj.getValues());
            }
            case SM_RECTANGLE -> {
                return pointInsideRectangle(x,y,(int[])obj.getValues());
            }
            case SM_ELLIPSE -> {
                return pointInsideEllipse(x,y,(int[])obj.getValues());
            }
            case SM_POLYGON -> {
                return pointInsidePolygon(x,y,(Object[])obj.getValues());
            }
            default -> {return false;}
        }
    }

    static boolean pointInsidePolygon(int px, int py, Object[] obj){
        //https://alienryderflex.com/polygon_fill/
        Vec2d[] v = (Vec2d[]) obj[0];
        float[] multiple = (float[]) obj[1];
        float[] constant = (float[]) obj[2];
        int vertCount = v.length;
        boolean oddNodes=false,current=v[vertCount-1].y>py,previous = false;
        for(int i=0; i<vertCount; i++){
            previous=current;
            current=v[i].y>py;
            if(current!=previous)
                oddNodes^= py * multiple[i] + constant[i] < px;
        }
        return oddNodes;
    }

    static boolean pointInsideRectangle(int px,int py,int[] v){
        int x1=v[0],y1=v[1],x2=v[2],y2=v[3];
        return (px >= x1 && px <= x2 && py >= y1 && py <= y2);
    }

    static boolean pointInsideEllipse(int px,int py,int[] v){
        int h = v[0],k = v[1], x = px, y = py, a = v[2], b = v[3];
        int point = (int)((Math.pow((x-h),2) / Math.pow(a,2)) + (Math.pow((y-k),2) / Math.pow(b,2)));
        return point <1;
    }

    static boolean pointInsideCircle(int px,int py,int[] v){
        int cx = v[0],cy = v[1],radie = v[2];
        return !((Math.pow((float) px - cx, 2) + Math.pow((float) py - cy, 2)) > Math.pow(radie, 2));
    }

    static boolean pointInsideTriangle(int px,int py,int[] v){
        int x1=v[0],x2=v[1],x3=v[2],y1=v[3],y2=v[4],y3=v[5];
        float d1,d2,d3;
        boolean has_neg,has_pos;
        d1 = sign((float)px,(float)py,
                (float)x1,(float)y1,
                (float)x2,(float)y2);
        d2 = sign((float)px,(float)py,
                (float)x2,(float)y2,
                (float)x3,(float)y3);
        d3 = sign((float)px,(float)py,
                (float)x3,(float)y3,
                (float)x1,(float)y1);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);
        return !(has_neg && has_pos);
    }

    static float sign(float x1,float y1,float x2,float y2,float x3,float y3){
        return (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3);
    }
}
