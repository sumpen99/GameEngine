package helper.struct;

public class PixelF{
    public float red,green,blue,alpha;

    public PixelF(float red,float green,float blue,float alpha){
        this.red = red * 255.0f;
        this.green = green * 255.0f;
        this.blue = blue * 255.0f;
        this.alpha = alpha * 255.0f;
    }

    public PixelF(){
        this.red = 255.0f;
        this.green = 255.0f;
        this.blue = 255.0f;
        this.alpha = 255.0f;
    }

    @Override
    public String toString(){
        return "[ Red: %f ] [ Green: %f ] [ Blue: %f ] [ Alpha: %f ]".formatted(red,green,blue,alpha);
    }


}
