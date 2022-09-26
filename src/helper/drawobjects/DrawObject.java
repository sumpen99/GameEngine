package helper.drawobjects;
import helper.enums.DrawMode;
import helper.enums.WidgetShape;
import helper.interfaces.IDrawObject;
import helper.struct.Vec2d;

public abstract class DrawObject implements IDrawObject{
    protected Vec2d pos;
    protected Vec2d size;
    protected Vec2d center;
    public WidgetShape shape;
    public DrawMode draw;
    public int color;
    public int opacity;
    public DrawObject(WidgetShape shape, DrawMode draw,int color,int opacity){
        this.shape = shape;
        this.draw = draw;
        this.color = color;
        this.opacity = opacity;
    }
    public void setNewPosition(int x,int y){}
    public void setNewPositionY(int y){}
    public void setNewPositionX(int x){}
    public Vec2d getPos(){return this.pos;}
    public Vec2d getSize(){return this.size;}
    public Vec2d getCenter(){return this.center;}
    public int alignAxisX(int width){
        int dx = (size.x-width)/2;
        return this.pos.x+dx;
    }
    public int alignAxisY(int height){
        int dy = (size.y-height)/2;
        return this.pos.y+dy;
    }
}
