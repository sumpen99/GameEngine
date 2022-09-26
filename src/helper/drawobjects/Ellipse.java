package helper.drawobjects;
import helper.canvas.CanvasHandler;
import helper.enums.DrawMode;
import helper.enums.WidgetShape;
import helper.struct.Vec2d;

public class Ellipse extends DrawObject {
    protected Vec2d radie;

    public Ellipse(int cx,int cy,int rx,int ry,int color,int opacity,DrawMode draw){
        super(WidgetShape.SM_ELLIPSE,draw,color,opacity);
        this.radie = new Vec2d(rx,ry);
        this.pos = new Vec2d(cx-rx,cy-ry);
        this.size = new Vec2d(rx*2,ry*2);
        this.setCenter();
    }

    @Override
    public void rePosition(Vec2d offset){
        this.pos.x += offset.x;
        this.pos.y += offset.y;
        this.setCenter();
    }

    @Override
    public void setCenter(){
        if(this.center == null){this.center = new Vec2d(0,0);}
        this.center.x = this.pos.x+this.radie.x;
        this.center.y = this.pos.y+this.radie.y;
    }

    @Override
    public int[] getValues(){
        return new int[] {this.center.x,this.center.y,this.radie.x,this.radie.y};
    }

    @Override
    public void draw(){
        if(this.draw == DrawMode.FILL){this.drawFilledEllipse();}
        else if(this.draw == DrawMode.OUTLINE){this.drawOutLinedEllipse();}
    }

    private void drawOutLinedEllipse(){
        float dx,dy,d1,d2,x,y;
        float rx,ry,cx,cy;
        cx = this.center.x;
        cy = this.center.y;
        ry = this.radie.y;
        rx = this.radie.x;
        x =  0;
        y = this.radie.y;

        d1 = (ry * ry) - (rx * rx * ry) + (0.25f * rx * rx);
        dx = 2.0f * ry * ry * x;
        dy = 2.0f * rx * rx * y;
        while(dx < dy){
            CanvasHandler.setPixel((int)(x + cx),(int)(y + cy),this.color);
            CanvasHandler.setPixel((int)(-x + cx),(int)(y + cy),this.color);
            CanvasHandler.setPixel((int)(x + cx),(int)(-y + cy),this.color);
            CanvasHandler.setPixel((int)(-x + cx),(int)(-y + cy),this.color);

            if(d1 < 0){
                x++;
                dx = dx + (2 * ry * ry);
                d1 = d1 + dx + (ry * ry);
            }
            else{
                x++;
                y--;
                dx = dx + (2.0f * ry * ry);
                dy = dy - (2 * rx * rx);
                d1 = d1 + dx - dy + (ry * ry);
            }
        }

        d2 = ((ry * ry) * ((x + 0.5f) * (x + 0.5f))) + ((rx * rx) * ((y - 1.0f) * (y - 1.0f))) - (rx * rx * ry * ry);

        while(y >= 0){
            CanvasHandler.setPixel((int)(x + cx),(int)(y + cy),this.color);
            CanvasHandler.setPixel((int)(-x + cx),(int)(y + cy),this.color);
            CanvasHandler.setPixel((int)(x + cx),(int)(-y + cy),this.color);
            CanvasHandler.setPixel((int)(-x + cx),(int)(-y + cy),this.color);
            if(d2 > 0){
                y--;
                dy = dy - (2.0f * rx * rx);
                d2 = d2 + (rx * rx) - dy;
            }
            else{
                y--;
                x++;
                dx = dx + (2 * ry * ry);
                dy = dy - (2 * rx * rx);
                d2 = d2 + dx - dy + (rx * rx);
            }

        }

    }

    private void drawFilledEllipse(){
        int left,right,top,bottom;
        int a,b,x,y;
        int old_y;
        int d1,d2;
        int a2,b2,a2b2,a2sqr,b2sqr,a4sqr,b4sqr;
        int a8sqr,b8sqr,a4sqr_b4sqr;
        int fn,fnw,fw;
        int fnn,fnnw,fnwn,fnwnw,fnww,fww,fwnw;

        left = this.center.x - this.radie.x;
        right = this.center.x + this.radie.x;
        top = this.center.y - this.radie.y;
        bottom = this.center.y + this.radie.y;

        a = (right-left)/2;
        b = (bottom-top)/2;

        x = 0;
        y = b;

        a2 = a * a;
        b2 = b * b;
        a2b2 = a2 + b2;
        a2sqr = a2 + a2;
        b2sqr = b2 + b2;
        a4sqr = a2sqr + a2sqr;
        b4sqr = b2sqr + b2sqr;
        a8sqr = a4sqr + a4sqr;
        b8sqr = b4sqr + b4sqr;
        a4sqr_b4sqr = a4sqr + b4sqr;

        fn = a8sqr + a4sqr;
        fnn = a8sqr;
        fnnw = a8sqr;
        fnw = a8sqr + a4sqr - b8sqr * a + b8sqr;
        fnwn = a8sqr;
        fnwnw = a8sqr + b8sqr;
        fnww = b8sqr;
        fwnw = b8sqr;
        fww = b8sqr;
        d1 = b2 - b4sqr * a + a4sqr;

        while((fnw < a2b2) || (d1 < 0) || ((fnw - fn > b2) && (y > 0)))
        {
            int px = left + x;
            int ex = right - x;
            while(px <= ex){
                CanvasHandler.setPixel(px,top+y,this.color);
                CanvasHandler.setPixel(px,bottom-y,this.color);
                px++;
            }

            y--;
            if((d1 < 0) || (fnw - fn > b2))
            {
                d1 += fn;
                fn += fnn;
                fnw += fnwn;
            }else{
                x++;
                d1 += fnw;
                fn += fnnw;
                fnw += fnwnw;
            }
        }

        fw = fnw - fn + b4sqr;
        d2 = d1 + (fw + fw - fn - fn + a4sqr_b4sqr + a8sqr) / 4;
        fnw += b4sqr - a4sqr;

        old_y = y + 1;

        while(x <= a)
        {
            if(y != old_y)
            {
                int px = left + x;
                int ex = right - x;
                while(px <= ex){
                    CanvasHandler.setPixel(px,top+y,this.color);
                    CanvasHandler.setPixel(px,bottom-y,this.color);
                    px++;
                }
            }

            old_y = y;
            x++;
            if(d2 < 0)
            {
                y--;
                d2 += fnw;
                fw += fwnw;
                fnw += fnwnw;
            }else{
                d2 += fw;
                fw += fww;
                fnw += fnww;
            }
        }

    }


}
