package helper.struct;

public class Point {
    public float x,y,length;
    public Point(float px,float py){
        x = px;
        y = py;
        length = (float)Math.sqrt(Math.pow(px,2)+Math.pow(py,2));
    }
    public Point(int px,int py){
        x = (float)px;
        y = (float)py;
        length = (float)Math.sqrt(Math.pow(px,2)+Math.pow(py,2));
    }

    public static boolean equalPoint(Point p1,Point p2){
        if(p1 == null || p2 == null){return false;}
        return(p1.x == p2.x && p1.y == p2.y);
    }

    public static Point subPoint(Point p1,Point p2){return new Point(p1.x-p2.x,p1.y-p2.y);}

    public static Point multPoint(Point p,float m){return new Point(p.x*m,p.y*m);}

    public static Point addPoint(Point p,float a){return new Point(p.x+a,p.y+a);}

    public static Point divPoint(Point p,float d){return new Point(p.x/d,p.y/d);}

    public static Point addPointPoint(Point p1,Point p2){return new Point(p1.x+p2.x,p1.y+p2.y);}
}
