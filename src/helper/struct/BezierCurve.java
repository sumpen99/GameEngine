package helper.struct;

public class BezierCurve {


    public static Point[] tessellate(Point p0,Point p1,Point p2){
        int subDivInto = 20,i=0;
        Point[] out = new Point[subDivInto+1];
        float stepPerIter = 1.0f/(float)subDivInto;
        while(i<=subDivInto){
            float t = i*stepPerIter;
            float t1 = (1.0f-t);
            float t2 = t*t;
            float x = t1*t1*p0.x + 2*t1*t*p1.x + t2*p2.x;
            float y = t1*t1*p0.y + 2*t1*t*p1.y + t2*p2.y;
            out[i] = new Point(x,y);
            i++;
        }
        return out;
    }
}
