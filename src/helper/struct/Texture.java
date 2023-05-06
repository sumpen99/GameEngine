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
                FVec2d vCell = FVec2d.sub(vDenorm,new FVec2d(0.5f,0.5f)).floor();
                FVec2d vOffset = FVec2d.sub(FVec2d.sub(vDenorm,vCell),new FVec2d(0.5f,0.5f));
                Pixel pixTL = getPixel(Vec2d.add(vCell,new FVec2d(0,0)),border);
                Pixel pixTR = getPixel(Vec2d.add(vCell,new FVec2d(1,0)),border);
                Pixel pixBL = getPixel(Vec2d.add(vCell,new FVec2d(0,1)),border);
                Pixel pixBR = getPixel(Vec2d.add(vCell,new FVec2d(1,1)),border);

                Pixel pixTX = Pixel.add(Pixel.mult(pixTR, vOffset.x),Pixel.mult(pixTL,(1.0f- vOffset.x)));
                Pixel pixBX = Pixel.add(Pixel.mult(pixBR, vOffset.x),Pixel.mult(pixBL,(1.0f- vOffset.x)));

                out = Pixel.add(Pixel.mult(pixBX, vOffset.y),Pixel.mult(pixTX,(1.0f- vOffset.y)));
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
