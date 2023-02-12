package helper.struct;
import helper.enums.BorderMethod;
import helper.enums.Color;
import helper.enums.SampleMethod;
import helper.io.IOHandler;


public class Texture {
    byte[] frameBuffer;
    ImageInfo imgInfo;
    FVec2d size;
    public void loadFromFile(String pathToImg){
        imgInfo = new ImageInfo();
        frameBuffer = IOHandler.readBytesFromImage(pathToImg,imgInfo);
        size = new FVec2d(imgInfo.width, imgInfo.height);
    }

    public Pixel getData(int index){
        int color = 0x0;
        if(index >= 0 && index < frameBuffer.length-3){
            color <<=8;
            color|=frameBuffer[index+3] & 0xff; // red
            color <<=8;
            color|=frameBuffer[index+2] & 0xff; // green
            color <<=8;
            color|=frameBuffer[index+1] & 0xff; // blue
            color <<=8;
            color|=frameBuffer[index] & 0xff;// alpha
            return new Pixel(color);
        }
        return new Pixel();
    }

    public int getIndex(Vec2d pos){
        return ((pos.y * imgInfo.width)+pos.x)*4;
    }

    public FVec2d getSize(){
        return this.size;
    }

    public Pixel sample(FVec2d vUV, SampleMethod sample, BorderMethod border){

        FVec2d vDenorm = FVec2d.mult(vUV,getSize());
        Pixel out = new Pixel();

        switch(sample){
            case POINT:{
                out = getPixel(new Vec2d(vDenorm),border);
                break;
            }
            case BILINEAR:{
                FVec2d vCell = vDenorm.floor();
                FVec2d vOffset = FVec2d.sub(vDenorm,vCell);
                break;
            }
            case BICUBIC:{
                break;
            }

        }
        return out;
    }

    public Pixel getPixel(Vec2d pos,BorderMethod border){
        Pixel out = new Pixel(Color.WHITE.getValue());
        switch(border){
            case BLANK:{
                if(pos.x >= 0 && pos.x < imgInfo.width &&
                    pos.y >= 0 && pos.y < imgInfo.height){
                    int index = getIndex(pos);
                    out = getData(index);
                }
                break;
            }
            case CLAMP:{
                Vec2d vClampedPos = pos.clamp(new Vec2d(0,0),new Vec2d(imgInfo.width-1, imgInfo.height-1));
                int index = getIndex(vClampedPos);
                out = getData(index);
                break;
            }
            case PERIODIC:{
                Vec2d vPeriodicPos = new Vec2d(
                        Math.abs(pos.x) % imgInfo.width,
                        Math.abs(pos.y) % imgInfo.height);
                int index = getIndex(vPeriodicPos);
                out = getData(index);
                break;
            }
            case REFLECTED_PERIODIC:{
                Vec2d vPeriodicPos = new Vec2d(
                        (pos.x<0) ? (imgInfo.width-1) - Math.abs(pos.x) % imgInfo.width : Math.abs(pos.x) % imgInfo.width,
                        (pos.y<0) ? (imgInfo.height-1) - Math.abs(pos.y) % imgInfo.height : Math.abs(pos.y) % imgInfo.height);
                int index = getIndex(vPeriodicPos);
                out = getData(index);
                break;
            }
        }
        return out;
    }


}
