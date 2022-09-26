package helper.drawobjects;
import helper.enums.DrawMode;
import helper.enums.WidgetShape;
import helper.struct.Vec2d;

public class Rectangle extends DrawObject{
    protected Vec2d p1;
    protected Vec2d p2;

    public Rectangle(int x, int y, int width, int height, int color,int opacity, DrawMode draw){
        super(WidgetShape.SM_RECTANGLE,draw,color,opacity);
        this.p1 = new Vec2d(x,y);
        this.p2 = new Vec2d(x+width,y+height);
        this.pos = new Vec2d(this.p1.x,this.p1.y);
        this.size = new Vec2d(width,height);
        this.setCenter();
    }

    @Override
    public void setNewPosition(int x,int y){
        this.p1.x = x;this.p2.x = x + size.x;
        this.p1.y = y;this.p2.y = y + size.y;
        this.setCenter();
    }

    @Override
    public void setNewPositionX(int x){
        this.p1.x = x;this.p2.x = x + size.x;
        this.setCenter();
    }

    @Override
    public void setNewPositionY(int y){
        this.p1.y = y;this.p2.y = y + size.y;
        this.setCenter();
    }

    @Override
    public void rePosition(Vec2d offset){
        this.p1.x+= offset.x;this.p2.x+= offset.x;
        this.p1.y+= offset.y;this.p2.y+= offset.y;
        this.setCenter();
    }

    @Override
    public void setCenter(){
        if(this.center == null){this.center = new Vec2d(0,0);}
        this.center.x = this.p1.x+this.size.x/2;
        this.center.y = this.p1.y+this.size.y/2;
        this.pos.x = this.p1.x;
        this.pos.y = this.p1.y;
    }

    @Override
    public int[] getValues(){
        return new int[] {this.p1.x,this.p1.y,this.p2.x,this.p2.y};
    }

    @Override
    public void draw(){
        if(this.draw == DrawMode.FILL){this.drawFilledRectangle();}
        else if(this.draw == DrawMode.OUTLINE){this.drawOutLinedRectangle();}
    }

    private void drawOutLinedRectangle(){
        Line.drawLine(this.p1.x,this.p1.y,this.p2.x,this.p1.y,this.color);
        Line.drawLine(this.p1.x,this.p1.y,this.p1.x,this.p2.y,this.color);
        Line.drawLine(this.p1.x,this.p2.y,this.p2.x,this.p2.y,this.color);
        Line.drawLine(this.p2.x,this.p2.y,this.p2.x,this.p1.y,this.color);
    }

    private void drawFilledRectangle(){
        for(int y = this.p1.y;y<this.p2.y;y++){Line.drawLine(this.p1.x,y,this.p2.x,y,this.color);}
    }

    public static void drawRectangleFilled(int x1,int y1,int width,int height,int color){
        for(int y = y1;y<y1+height;y++){Line.drawLine(x1,y,x1+width,y,color);}
    }

    public static void drawRectangleOutLined(int x1,int y1,int width,int height,int color){
        int x2 = x1+width,y2 = y1+height;
        Line.drawLine(x1,y1,x2,y1,color);
        Line.drawLine(x1,y1,x1,y2,color);
        Line.drawLine(x1,y2,x2,y2,color);
        Line.drawLine(x2,y2,x2,y1,color);
    }


}
