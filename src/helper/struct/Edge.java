package helper.struct;

public class Edge{
    public Vec2d e1;
    public Vec2d e2;
    public Edge(int x1,int y1,int x2,int y2){
        this.e1 = new Vec2d(x1,y1);
        this.e2 = new Vec2d(x2,y2);
    }
}
