package helper.drawobjects;
import helper.canvas.CanvasHandler;
import helper.enums.Color;
import helper.enums.DrawMode;
import helper.enums.WidgetShape;
import helper.io.IOHandler;
import helper.struct.Tri;
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

    public static void texturedTriangle(Tri tri,byte[] texture,int bufWidth,int bufHeight,int color){
        tri.swapPos1Pos2();
        tri.swapPos3Pos1();
        tri.swapPos3Pos2();
        int dx1,dx2,dy1,dy2;
        float du1,du2,dv1,dv2,dw1,dw2;
        float tex_u,tex_v,tex_w;
        float dax_step,dbx_step,du1_step,dv1_step,du2_step,dv2_step,dw1_step,dw2_step;

        dax_step = 0.0f;
        dbx_step= 0.0f;
        du1_step= 0.0f;
        dv1_step= 0.0f;
        du2_step= 0.0f;
        dv2_step= 0.0f;
        dw1_step= 0.0f;
        dw2_step= 0.0f;

        dx1 = (int)(tri.p2.x-tri.p1.x);
        dy1 = (int)(tri.p2.y-tri.p1.y);
        du1 = tri.p2.u-tri.p1.u;
        dv1 = tri.p2.v-tri.p1.v;
        dw1 = tri.p2.w-tri.p1.w;

        dx2 = (int)(tri.p3.x-tri.p1.x);
        dy2 = (int)(tri.p3.y-tri.p1.y);
        du2 = tri.p3.u-tri.p1.u;
        dv2 = tri.p3.v-tri.p1.v;
        dw2 = tri.p3.w-tri.p1.w;

        if (dy1 != 0){dax_step = (float)dx1 / Math.abs(dy1);}
        if (dy2 != 0){dbx_step = (float)dx2 / Math.abs(dy2);}
        if (dy1 != 0){du1_step = du1 / Math.abs(dy1);}
        if (dy1 != 0){dv1_step = dv1 / Math.abs(dy1);}
        if (dy1 != 0){dw1_step = dw1 / Math.abs(dy1);}

        if (dy2 != 0){du2_step = du2 / Math.abs(dy2);}
        if (dy2 != 0){dv2_step = dv2 / Math.abs(dy2);}
        if (dy2 != 0){dw2_step = dw2 / Math.abs(dy2);}

        if(dy1 != 0){
            int i = (int)tri.p1.y,stop = (int)tri.p2.y;
            while(i<stop){
                int ax = (int)(tri.p1.x + ((float)i - tri.p1.y) * dax_step);
                int bx = (int)(tri.p1.x + ((float)i - tri.p1.y) * dbx_step);

                float tex_su = tri.p1.u + ((float)i - tri.p1.y) * du1_step;
                float tex_sv = tri.p1.v + ((float)i - tri.p1.y) * dv1_step;
                float tex_sw = tri.p1.w + ((float)i - tri.p1.y) * dw1_step;

                float tex_eu = tri.p1.u + ((float)i - tri.p1.y) * du2_step;
                float tex_ev = tri.p1.v + ((float)i - tri.p1.y) * dv2_step;
                float tex_ew = tri.p1.w + ((float)i - tri.p1.y) * dw2_step;
                if(ax > bx){
                    int tempX;
                    float tempSu,tempSv,tempSw;
                    tempX = ax;
                    ax = bx;
                    bx = tempX;

                    tempSu = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempSu;

                    tempSv = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempSv;

                    tempSw = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempSw;

                }
                float tstep = 0.0f;
                if(bx-ax != 0){tstep = 1.0f / (float)(bx-ax);}
                float t = 0.0f;
                int j = ax;
                while(j < bx){
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;
                    int sampleX = (int)((tex_u/tex_w)*bufWidth);
                    int sampleY = (int)((tex_v/tex_w)*bufHeight);
                    int index = (sampleY*bufWidth)+sampleX;
                    if(texture[index] < 0){
                        CanvasHandler.setPixel(j,i,color);
                    }
                    t += tstep;
                    j+=1;
                }
                i+=1;
            }

        }

        dy1 = (int)(tri.p3.y - tri.p2.y);
        dx1 = (int)(tri.p3.x - tri.p2.x);
        dv1 = tri.p3.v - tri.p2.v;
        du1 = tri.p3.u - tri.p2.u;
        dw1 = tri.p3.w - tri.p2.w;

        if(dy1 != 0){dax_step = (float)dx1 / Math.abs(dy1);}
        if(dy2 != 0){dbx_step = (float)dx2 / Math.abs(dy2);}
        du1_step = 0.0f;
        dv1_step = 0.0f;
        if(dy1 != 0){du1_step = du1 / Math.abs(dy1);}
        if (dy1 != 0){dv1_step = dv1 / Math.abs(dy1);}
        if (dy1 != 0){dw1_step = dw1 / Math.abs(dy1);}

        if(dy1 != 0){
            int i = (int)tri.p2.y,stop = (int)tri.p3.y;

            while(i<stop) {
                int ax = (int) (tri.p2.x + ((float) i - tri.p2.y) * dax_step);
                int bx = (int) (tri.p1.x + ((float) i - tri.p1.y) * dbx_step);

                float tex_su = tri.p2.u + ((float) i - tri.p2.y) * du1_step;
                float tex_sv = tri.p2.v + ((float) i - tri.p2.y) * dv1_step;
                float tex_sw = tri.p2.w + ((float) i - tri.p2.y) * dw1_step;

                float tex_eu = tri.p1.u + ((float) i - tri.p1.y) * du2_step;
                float tex_ev = tri.p1.v + ((float) i - tri.p1.y) * dv2_step;
                float tex_ew = tri.p1.w + ((float) i - tri.p1.y) * dw2_step;

                if (ax > bx) {
                    int tempX;
                    float tempSu, tempSv, tempSw;
                    tempX = ax;
                    ax = bx;
                    bx = tempX;

                    tempSu = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempSu;

                    tempSv = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempSv;

                    tempSw = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempSw;

                }

                float tstep = 0.0f;
                if (bx - ax != 0) {tstep = 1.0f / ((float)bx - (float)ax);}

                float t = 0.0f;
                int j = ax;
                while(j < bx){
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;
                    int sampleX = (int)((tex_u/tex_w)*bufWidth);
                    int sampleY = (int)((tex_v/tex_w)*bufHeight);
                    int index = (sampleY*bufWidth)+sampleX;
                    if(texture[index] < 0){
                        CanvasHandler.setPixel(j,i, color);
                    }
                    t += tstep;
                    j++;
                }
                i++;
            }

        }

    }

}
