package helper.struct;

public class Edge{
    public Point p1;
    public Point p2;
    public Edge(float x1,float y1,float x2,float y2){
        this.p1 = new Point(x1,y1);
        this.p2 = new Point(x2,y2);
    }
}
