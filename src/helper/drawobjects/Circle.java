package helper.drawobjects;
import helper.canvas.CanvasHandler;
import helper.enums.DrawMode;
import helper.enums.Mask;
import helper.enums.WidgetShape;
import helper.struct.Vec2d;

public class Circle extends DrawObject{
    protected int radius;
    protected Mask mask;

    public Circle(int cx, int cy, int radius, int color,int opacity, DrawMode draw) {
        super(WidgetShape.SM_CIRCLE,draw,color,opacity);
        this.radius = radius;
        this.mask = Mask.DEG_360;
        this.pos = new Vec2d(cx-radius,cy-radius);
        this.size = new Vec2d(radius*2,radius*2);
        this.setCenter();
    }

    @Override
    public int[] getValues(){return new int[] {this.center.x,this.center.y,this.radius};}

    @Override
    public void rePosition(Vec2d offset){
        this.pos.x += offset.x;
        this.pos.y+=offset.y;
        this.setCenter();
    }

    @Override
    public void setCenter(){
        if(this.center == null){this.center = new Vec2d(0,0);}
        this.center.x = this.pos.x+this.radius;
        this.center.y = this.pos.y+this.radius;
    }

    @Override
    public void draw(){
        if(this.draw == DrawMode.FILL){this.drawFilledCircle();}
        else if(this.draw == DrawMode.OUTLINE){this.drawOutLinedCircle();}
    }

    private void drawOutLinedCircle()
    {
        int x = this.center.x;
        int y = this.center.y;
        int x0 = 0;
        int y0 = this.radius;
        int d = 3-(2*this.radius);

        if(this.radius == 0)
            return;
        while(y0 >= x0)
        {
            // Bit 1 & 1 = 1, 1 & 0 = 0
            if((this.mask.bit & Mask.DEG_1_45.bit) == Mask.DEG_1_45.bit) CanvasHandler.setPixel(x+x0,y-y0,this.color);
            if((this.mask.bit & Mask.DEG_2_45.bit) == Mask.DEG_2_45.bit) CanvasHandler.setPixel(x+y0,y-x0,this.color);

            if((this.mask.bit & Mask.DEG_3_45.bit) == Mask.DEG_3_45.bit) CanvasHandler.setPixel(x+y0,y+x0,this.color);
            if((this.mask.bit & Mask.DEG_4_45.bit) == Mask.DEG_4_45.bit) CanvasHandler.setPixel(x+x0,y+y0,this.color);

            if((this.mask.bit & Mask.DEG_5_45.bit) == Mask.DEG_5_45.bit) CanvasHandler.setPixel(x-x0,y+y0,this.color);
            if((this.mask.bit & Mask.DEG_6_45.bit) == Mask.DEG_6_45.bit) CanvasHandler.setPixel(x-y0,y+x0,this.color);

            if((this.mask.bit & Mask.DEG_7_45.bit) == Mask.DEG_7_45.bit) CanvasHandler.setPixel(x-y0,y-x0,this.color);
            if((this.mask.bit & Mask.DEG_8_45.bit) == Mask.DEG_8_45.bit) CanvasHandler.setPixel(x-x0,y-y0,this.color);
            if(d <0 ) d+=4*x0++ + 6;
            else d+=4*(x0++ - y0--) + 10;

        }



    }

    private void drawFilledCircle() {
        //https://github.com/OneLoneCoder/olcPixelGameEngine/blob/master/olcPixelGameEngine.h FillCircle
        int x0 = 0;
        int y0 = this.radius;
        int d = 3 - 2*this.radius;
        if(this.radius == 0)return;
        while(y0 >= x0)
        {
            drawLineInCircle(this.center.x-x0,this.center.x+x0,this.center.y-y0,this.color);
            drawLineInCircle(this.center.x-y0,this.center.x+y0,this.center.y-x0,this.color);
            drawLineInCircle(this.center.x-x0,this.center.x+x0,this.center.y+y0,this.color);
            drawLineInCircle(this.center.x-y0,this.center.x+y0,this.center.y+x0,this.color);
            if(d<0)d+= 4*x0++ + 6;
            else d+=4*(x0++ - y0--) + 10;

        }

    }

    public static void drawFilledCircle(int cx,int cy,int radius,int color) {
        int x0 = 0;
        int y0 = radius;
        int d = 3 - 2*radius;
        if(radius == 0)return;
        while(y0 >= x0)
        {
            drawLineInCircle(cx-x0,cx+x0,cy-y0,color);
            drawLineInCircle(cx-y0,cx+y0,cy-x0,color);
            drawLineInCircle(cx-x0,cx+x0,cy+y0,color);
            drawLineInCircle(cx-y0,cx+y0,cy+x0,color);
            if(d<0)d+= 4*x0++ + 6;
            else d+=4*(x0++ - y0--) + 10;

        }

    }

    private static void drawLineInCircle(int sx,int ex,int ny,int color)
    {
        for(int i = sx;i<ex;i++)
            CanvasHandler.setPixel(i,ny,color);
    }
}

