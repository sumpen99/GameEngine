package helper.struct;

public class FVec2d {
    public float x,y;

    public FVec2d(float x,float y){
        this.x = x;
        this.y = y;
    }

    public FVec2d(){
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public static FVec2d add(FVec2d fv1,FVec2d fv2){
        return new FVec2d(fv1.x+ fv2.x,fv1.y+ fv2.y);
    }

    public static FVec2d mult(FVec2d fv1,FVec2d fv2){
        return new FVec2d(fv1.x * fv2.x,fv1.y * fv2.y);
    }

    public void floor(){
        this.x = (int)this.x;
        this.y = (int)this.y;
    }

}
