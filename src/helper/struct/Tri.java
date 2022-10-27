package helper.struct;

public class Tri {
    public Vec3d p1,p2,p3;

    public Tri(float x1,float y1,float u1,float v1,float x2,float y2,float u2,float v2,float x3,float y3,float u3,float v3){
        p1 = new Vec3d(x1,y1,0.0f,u1,v1,1.0f);
        p2 = new Vec3d(x2,y2,0.0f,u2,v2,1.0f);
        p3 = new Vec3d(x3,y3,0.0f,u3,v3,1.0f);
    }

    public void swapPos1Pos2(){
        if(p2.y >= p1.y)return;
        float tx,ty,tz,tu,tv,tw;
        tx = p1.x;
        ty = p1.y;
        tz = p1.z;
        tu = p1.u;
        tv = p1.v;
        tw = p1.w;
        p1.x = p2.x;
        p1.y = p2.y;
        p1.z = p2.z;
        p1.u = p2.u;
        p1.v = p2.v;
        p1.w = p2.w;

        p2.x = tx;
        p2.y = ty;
        p2.z = tz;
        p2.u = tu;
        p2.v = tv;
        p2.w = tw;
    }

    public void swapPos3Pos1(){
        if(p3.y >= p1.y)return;
        float tx,ty,tz,tu,tv,tw;
        tx = p1.x;
        ty = p1.y;
        tz = p1.z;
        tu = p1.u;
        tv = p1.v;
        tw = p1.w;
        p1.x = p3.x;
        p1.y = p3.y;
        p1.z = p3.z;
        p1.u = p3.u;
        p1.v = p3.v;
        p1.w = p3.w;

        p3.x = tx;
        p3.y = ty;
        p3.z = tz;
        p3.u = tu;
        p3.v = tv;
        p3.w = tw;
    }

    public void swapPos3Pos2(){
        if(p3.y >= p2.y)return;
        float tx,ty,tz,tu,tv,tw;
        tx = p2.x;
        ty = p2.y;
        tz = p2.z;
        tu = p2.u;
        tv = p2.v;
        tw = p2.w;
        p2.x = p3.x;
        p2.y = p3.y;
        p2.z = p3.z;
        p2.u = p3.u;
        p2.v = p3.v;
        p2.w = p3.w;

        p3.x = tx;
        p3.y = ty;
        p3.z = tz;
        p3.u = tu;
        p3.v = tv;
        p3.w = tw;
    }
}
