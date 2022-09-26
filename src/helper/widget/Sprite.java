package helper.widget;
import helper.canvas.CanvasHandler;
import helper.drawobjects.DrawObject;
import helper.enums.Callback;
import helper.enums.WidgetType;
import helper.io.IOHandler;
import helper.struct.ImageInfo;
import helper.struct.Matrix;
import helper.struct.Matrix3X3;
import helper.struct.Vec2d;

public abstract class Sprite extends Widget{
    float ftheta,fscale_x,fscale_y,fthetaval;
    boolean doRotate,doScale;
    ImageInfo imgInfo;
    Matrix matSprite;
    byte[] frameBuffer;
    public Sprite(Object obj, Callback callback, WidgetType type, DrawObject shape,String pathToImg){
        super(obj,callback,type,shape);
        if(pathToImg != null){
            imgInfo = new ImageInfo();
            frameBuffer = IOHandler.readBytesFromImage(pathToImg,imgInfo);
            assert frameBuffer != null : "Error Reading Image On Path %s".formatted(pathToImg);
            setScale(true);
            setRotationValue(0.01f);
            setScaleValue(((float)getSize().x/(float)imgInfo.width),((float)getSize().y/(float)imgInfo.height));
        }
    }

    protected void printImageInfo(){IOHandler.printImageInfo(imgInfo);}
    protected void setScaleValue(float x,float y){
        fscale_x = x;
        fscale_y = y;
    }
    protected void setRotationValue(float value){fthetaval = value;}
    protected void setRotation(boolean value){
        doRotate = value;
    }
    protected void setScale(boolean value){
        doScale = value;
    }

    void translateSprite(){
        Matrix matCenter,matScale,matRotate,matMultRot,matMultScale,matOffset;
        matCenter = new Matrix3X3();
        matScale = new Matrix3X3();
        matRotate = new Matrix3X3();
        matMultRot = new Matrix3X3();
        matMultScale = new Matrix3X3();
        matOffset = new Matrix3X3();
        matSprite = new Matrix3X3();

        matCenter.translate(-(float)imgInfo.width/2,-(float)imgInfo.height/2);
        matRotate.rotate(ftheta);
        matMultRot.multiply(matRotate,matCenter);
        matScale.scale(fscale_x,fscale_y);
        matMultScale.multiply(matScale,matMultRot);

        matOffset.translate(getCenter().x,getCenter().y);

        matSprite.multiply(matOffset,matMultScale);

        if(doRotate){ftheta-=fthetaval;}
        /*Vec2d p = new Vec2d(0,0);
        matSprite.forward(0.0f,0.0f,s);
        e.x=s.x;
        e.y=s.y;

        matSprite.forward(imgInfo.width,imgInfo.height,p);
        s.x = Math.min(s.x,p.x);
        s.y = Math.min(s.y,p.y);
        e.x = Math.max(e.x,p.x);
        e.y = Math.max(e.y,p.y);

        matSprite.forward(0.0f,imgInfo.height,p);
        s.x = Math.min(s.x,p.x);
        s.y = Math.min(s.y,p.y);
        e.x = Math.max(e.x,p.x);
        e.y = Math.max(e.y,p.y);

        matSprite.forward(imgInfo.width,0.0f,p);
        s.x = Math.min(s.x,p.x);
        s.y = Math.min(s.y,p.y);
        e.x = Math.max(e.x,p.x);
        e.y = Math.max(e.y,p.y);*/

    }

    int sampleColor(int x,int y){
        int color = 0;
        if((x >= 0 && x < imgInfo.width) && (y >= 0 && y < imgInfo.height)) {
            int index = (y * imgInfo.width + x) * imgInfo.chanels;
            if (index >= 0 && (index + imgInfo.chanels) < imgInfo.size){
                return bytesToInt(index);
            }
        }
        return color;
    }

    int bytesToInt(int index){
        int color = 0x0;
       color <<=8;
       color|=frameBuffer[index+3] & 0xff; // red
       color <<=8;
       color|=frameBuffer[index+2] & 0xff; // green
       color <<=8;
       color|=frameBuffer[index+1] & 0xff; // blue
       color <<=8;
       color|=frameBuffer[index] & 0xff;// alpha

        return color;
    }

    @Override
    public boolean onMouseScrollUp(int x,int y){
        if(insideWidget(x,y)){
            fscale_x+=.005f;
            fscale_y+=.005f;
        }
        return false;
    }

    @Override
    public boolean onMouseScrollDown(int x,int y){
        if(insideWidget(x,y)){
            fscale_x-=.005f;
            fscale_y-=.005f;
        }
        return false;
    }

    @Override
    public void resetWidgetState(){
        doRotate = false;
    }

    @Override
    public String getBindingValue(){return null;}

    @Override
    public void setBindingValue(Object value){
        doRotate = (boolean)value;
    }

    @Override
    public void reposition(Vec2d offset){
        this.wObj.rePosition(offset);
        translateSprite();
    }

    @Override
    public void draw(){
        if(this.wObj.opacity != 0){
            //wObj.draw();
            translateSprite();
            drawSprite();
        }
    }

    void drawSprite(){
        Matrix matInvert = new Matrix3X3();
        matInvert.invert(matSprite);

        int x,y,sx,sy,ex,ey;
        Vec2d n = new Vec2d(0,0);
        sx = getPos().x;
        sy = getPos().y;
        ex = getPos().x + getSize().x;
        ey = getPos().y + getSize().y;
        x = sx;
        while(x < ex){
            y = sy;
            while(y < ey){
                matInvert.forward(x,y,n);
                int color = sampleColor(n.x, n.y);
                CanvasHandler.setPixel(x, y, color);
                y+=1;
            }
            x++;
        }
    }
}
