package helper.drawobjects;
import helper.canvas.CanvasHandler;
import helper.enums.DrawMode;
import helper.enums.WidgetShape;
import helper.struct.Vec2d;
import helper.struct.VecMinMax;

import java.lang.Math;

public class Triangle extends DrawObject{
    protected Vec2d p1;
    protected Vec2d p2;
    protected Vec2d p3;
    protected VecMinMax minMax;
    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3, int color,int opacity, DrawMode draw) {
        super(WidgetShape.SM_TRIANGLE,draw,color,opacity);
        this.p1 = new Vec2d(x1,y1);
        this.p2 = new Vec2d(x2,y2);
        this.p3 = new Vec2d(x3,y3);
        setMinMax();
        this.pos = new Vec2d(this.minMax.minX,this.minMax.minY);
        this.size = new Vec2d(this.minMax.maxY-this.minMax.minX,this.minMax.maxY-this.minMax.minY);
        this.setCenter();

    }

    @Override
    public void rePosition(Vec2d offset){
        this.p1.x+= offset.x;this.p2.x+= offset.x;this.p3.x+= offset.x;
        this.p1.y+= offset.y;this.p2.y+= offset.y;this.p3.y+= offset.y;
        this.setCenter();
    }

    @Override
    public void setCenter(){
        float x = (float)(this.p1.x+this.p2.x+this.p3.x)/3;
        float y = (float)(this.p1.y+this.p2.y+this.p3.y)/3;
        if(this.center == null){this.center = new Vec2d(0,0);}
        this.center.x = (int)x;
        this.center.y = (int)y;
        setMinMax();
        this.pos.x = this.minMax.minX;
        this.pos.y = this.minMax.minY;
    }

    @Override
    public int[] getValues(){
        return new int[] {this.p1.x,this.p2.x,this.p3.x,this.p1.y,this.p2.y,this.p3.y};
    }

    private void setMinMax(){
        int a,b,c,d;
        this.minMax = new VecMinMax(0,0,0,0);
        a = Math.min(this.p1.x,this.p2.x);
        a = Math.min(a,this.p3.x);
        b = Math.max(this.p1.x,this.p2.x);
        b = Math.max(b,this.p3.x);
        c = Math.min(this.p1.y,this.p2.y);
        c = Math.min(c,this.p3.y);
        d = Math.max(this.p1.y,this.p2.y);
        d = Math.max(d,this.p3.y);
        this.minMax.minX = a;
        this.minMax.maxX = b;
        this.minMax.minY = c;
        this.minMax.maxY = d;
    }

    @Override
    public void draw(){
        if(this.draw == DrawMode.FILL){this.drawFilledTriangle();}
        else if(this.draw == DrawMode.OUTLINE){this.drawOutLinedTriangle();}
    }

    private void drawOutLinedTriangle(){
        Line.drawLine(this.p1.x,this.p1.y,this.p2.x,this.p2.y,this.color);
        Line.drawLine(this.p2.x,this.p2.y,this.p3.x,this.p3.y,this.color);
        Line.drawLine(this.p3.x,this.p3.y,this.p1.x,this.p1.y,this.color);
    }

    private void drawFilledTriangle() {
        int x1 = this.p1.x,x2 = this.p2.x,x3 = this.p3.x;
        int y1 = this.p1.y,y2 = this.p2.y,y3 = this.p3.y;
        if(y2<y1){
            x1=x1^x2^(x2=x1);
            y1=y1^y2^(y2=y1);
        }
        if(y3<y1){
            x1=x1^x3^(x3=x1);
            y1=y1^y3^(y3=y1);
        }
        if(y3<y2){
            x2=x2^x3^(x3=x2);
            y2=y2^y3^(y3=y2);
        }

        int dy1 = y2-y1;
        int dx1 = x2-x1;

        int dy2 = y3-y1;
        int dx2 = x3-x1;

        float dax_step = 0.0f, dbx_step = 0.0f;

        if(dy1>0)
            dax_step = dx1/(float)Math.abs(dy1);
        if(dy2>0)
            dbx_step = dx2/(float)Math.abs(dy2);

        if(dy1>0)
        {
            for(int i = y1;i <= y2;i++) {
                int ax = (int)(x1 + (i-y1) * dax_step);
                int bx = (int)(x1 + (i-y1) * dbx_step);

                if(ax > bx){ax=ax^bx^(bx=ax);}

                for(int j = ax;j< bx;j++){
                    CanvasHandler.setPixel(j,i,this.color);
                }
            }
        }

        dy1 = y3-y2;
        dx1 = x3-x2;

        if(dy1 > 0) {
            dax_step = dx1 / (float) Math.abs(dy1);
        }
        if(dy2 > 0){
            dbx_step = dx2/(float)Math.abs(dy2);
        }

        if(dy1 > 0)
        {
            for(int i = y2;i <= y3;i++)
            {
                int ax = (int)(x2 + (i-y2) * dax_step);
                int bx = (int)(x1 + (i-y1) * dbx_step);

                if(ax > bx){ax=ax^bx^(bx=ax);}

                for(int j = ax;j< bx;j++)
                    CanvasHandler.setPixel(j,i,this.color);
            }
        }
    }

}
