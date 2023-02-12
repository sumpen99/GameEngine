package helper.struct;

public class Vec2d{
    public int x,y;
    public Vec2d(int x,int y){
        this.x = x;
        this.y = y;
    }
    public Vec2d(float x,float y){
        this.x = (int)x;
        this.y = (int)y;
    }
    public Vec2d(FVec2d fv){
        this.x = (int)fv.x;
        this.y = (int)fv.y;
    }
    public Vec2d(){}

    public Vec2d clamp(Vec2d v1,Vec2d v2){
        int x = Math.min(Math.max(this.x,v1.x),v2.x);
        int y = Math.min(Math.max(this.y,v1.y),v2.y);
        return new Vec2d(x,y);
    }

}
