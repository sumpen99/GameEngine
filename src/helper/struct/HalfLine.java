package helper.struct;
import static helper.struct.Point.addPoint;
import static helper.struct.Point.subPoint;
import static helper.struct.Point.multPoint;
import static helper.struct.Point.divPoint;
import static helper.struct.Point.addPointPoint;
public class HalfLine {
    public Point tangent,p,r,n,ext,half;
    public float a,b,c,dx,dy,length,angle,angleAngle,scale;
    public HalfLine(Point p,Point r){
        this.p = p;
        this.r = r;
        scale = 1.0f;
        updateValues();
    }

    public void updateValues(){
        tangent = subPoint(r,p);
        a = -tangent.y;
        b = tangent.x;
        c = p.x*a + p.y*b;
        Point nn = new Point(a,b);
        n = divPoint(nn,nn.length);
        calcLengthOfLine();
        calcAngleOfLine();
    }

    public HalfLine offset(float t){return new HalfLine(addPointPoint(p,multPoint(n,t)),addPointPoint(r,multPoint(n,t)));}

    public Point intersect(HalfLine other){
        float det = a * other.b - other.a * b;
        if(Math.abs(det)< 1e-5){return null;}
        float x = (other.b * c - b * other.c) / det;
        float y = (a * other.c - other.a * c) / det;
        return new Point(x,y);

    }

    public void rotateLine(float angle,float base){
        //base = 1
        if(base == 1){
            //x = cos(angle)*self.r.x - sin(angle)*self.r.y
            //y = sin(angle)*self.r.x + cos(angle)*self.r.y
            float x = (float)(length*Math.sin(angle) + p.x);
            float y = (float)(length*Math.cos(angle) + p.y);
            r.x = x;
            r.y = y;
        }
        else{
            //x = cos(angle)*self.p.x - sin(angle)*self.p.y
            //y = sin(angle)*self.p.x + cos(angle)*self.p.y
            float x = (float)(length*Math.sin(angle) + r.x);
            float y = (float)(length*Math.cos(angle) + r.y);
            p.x = x;
            p.y = y;
        }
        updateValues();
    }


    public void calcLengthOfLine(){
        length = (float)(Math.sqrt((r.x - p.x) * (r.x - p.x) + (r.y - p.y) * (r.y - p.y))/scale);
        half = new Point((r.x + p.x)/2,(r.y + p.y)/2);
    }

    public void calcAngleOfLine(){
        float dy = r.y - p.y;
        float dx = r.x - p.x;
        float theta = (float)Math.atan2(dy,dx);
        theta*=180/Math.PI;
        angle = 90 + theta;
        this.dx = dx;
        this.dy = dy;
    }


    public void angleBetweenLines(HalfLine l2){
        float angle = (float)Math.atan2(dx * l2.dy - l2.dx * dy,dx * l2.dx + dy * l2.dy);
        angleAngle = (float)(Math.abs(angle)*(180/Math.PI));
    }

    public void extendLine(float len){
        float cx = r.x + (r.x - p.x) / length * len;
        float cy = r.y + (r.y - p.y) / length * len;
        ext = new Point(cx,cy);
        half = new Point((ext.x + p.x)/2,(ext.y + p.y)/2);
    }

    public void extendLineAndKeep(float len){
        float cx = r.x + (r.x - p.x) / length * len;
        float cy = r.y + (r.y - p.y) / length * len;
        ext = new Point(cx,cy);
        half = new Point((ext.x + p.x)/2,(ext.y + p.y)/2);
        r = ext;
        updateValues();
    }

    public void halfPoint(){
        float cx = r.x + (r.x - p.x) / length;
        float cy = r.y + (r.y - p.y) / length;
        ext = new Point(cx,cy);
    }
}
