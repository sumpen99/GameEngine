package helper.drawobjects;
import helper.canvas.CanvasHandler;
import helper.enums.DrawMode;
import helper.enums.WidgetShape;
import helper.io.IOHandler;
import helper.struct.Vec2d;

import java.lang.Math;

public class Line extends DrawObject{
    Vec2d p1;
    Vec2d p2;
    public Line(int x1,int y1,int x2,int y2,int color,int opacity){
        super(WidgetShape.SM_LINE, DrawMode.OUTLINE,color,opacity);
        this.p1 = new Vec2d(x1,y1);
        this.p2 = new Vec2d(x2,y2);
        this.pos = new Vec2d(Math.min(this.p1.x,this.p2.x),Math.min(this.p1.y,this.p2.y));
        this.size = new Vec2d(Math.abs(this.p2.x-this.p1.x),Math.abs(this.p2.y-this.p1.y));
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
        this.center.x = (this.p1.x+this.p2.x)/2;
        this.center.y = (this.p1.y+this.p2.y)/2;
        this.pos.x = Math.min(this.p1.x,this.p2.x);
        this.pos.y = Math.min(this.p1.y,this.p2.y);
    }

    @Override
    public int[] getValues(){
        return new int[] {this.p1.x,this.p1.y,this.p2.x,this.p2.y};
    }

    @Override
    public void draw(){
        if(this.opacity != 0){Line.drawLine(this.p1.x,this.p1.y,this.p2.x,this.p2.y,this.color);}
    }

    public static void drawLine(int x1,int y1,int x2,int y2,int color){
        //https://github.com/OneLoneCoder/olcPixelGameEngine/blob/master/olcPixelGameEngine.h DrawLine
        int x, y, dx, dy, dx1, dy1, px, py, xe, ye, i;
        dx = x2 - x1; dy = y2 - y1;
        // Line is vertical
        if(dx == 0){
            if(y2 < y1){y1=y1^y2^(y2=y1);} // swap int instead of swap(int *x,int *y)
            for(y = y1;y<= y2; y++){CanvasHandler.setPixel(x1,y,color);}
            return;
        }
        // Line is horizontal
        if(dy == 0){
            if(x2 < x1){x1=x1^x2^(x2=x1);} // swap int
            for(x = x1; x <= x2; x++){CanvasHandler.setPixel(x,y1,color);}
            return;
        }

        dx1 = Math.abs(dx); dy1 = Math.abs(dy);
        px = 2 * dy1 - dx1;	py = 2 * dx1 - dy1;
        if(dy1 <= dx1){
            if (dx >= 0){x = x1; y = y1; xe = x2;}
            else{x = x2; y = y2; xe = x1;}

            CanvasHandler.setPixel(x,y,color);

            for (i = 0; x < xe; i++){
                x = x + 1;
                if(px < 0){px = px + 2 * dy1;}
                else{
                    if((dx < 0 && dy < 0) || (dx > 0 && dy > 0)){y = y + 1;}
                    else{y = y - 1;}
                    px = px + 2 * (dy1 - dx1);
                }
                CanvasHandler.setPixel(x,y,color);
            }
        }
        else{
            if (dy >= 0){x = x1; y = y1; ye = y2;}
            else{x = x2; y = y2; ye = y1;}

            CanvasHandler.setPixel(x,y,color);

            for(i = 0; y < ye; i++) {
                y = y + 1;
                if(py <= 0){py = py + 2 * dx1;}
                else{
                    if((dx < 0 && dy < 0) || (dx > 0 && dy > 0)){x = x + 1;}
                    else{x = x - 1;}
                    py = py + 2 * (dx1 - dy1);
                }
                CanvasHandler.setPixel(x,y,color);
            }
        }
    }
}



