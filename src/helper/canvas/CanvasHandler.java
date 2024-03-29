package helper.canvas;
import helper.io.IOHandler;
import helper.struct.Pixel;
import helper.struct.PixelF;
import helper.struct.Vec2d;

import java.awt.image.BufferedImage;
import java.awt.color.ColorSpace;
import java.awt.Transparency;
import java.awt.image.WritableRaster;
import java.awt.image.Raster;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

public class CanvasHandler{
    private BufferedImage imgBuffer;
    private byte[] frameBuffer;
    private int screenWidth,screenHeight;
    private static CanvasHandler self = null;
    private static boolean isSet = false;

    public CanvasHandler(){
        assert !CanvasHandler.isSet :"CanvasHandler is already set!";
        CanvasHandler.setInstance();
    }

    private static void setInstance(){
        CanvasHandler.isSet = true;
    }

    public static void initCanvasHandler(int width,int height){
        if(self == null){
            self = new CanvasHandler();
            self.screenWidth = width;
            self.screenHeight = height;
            self.setPixelData();
        }
    }

    public static boolean frameIsNull(){
        return (self.imgBuffer == null);
    }

    public static BufferedImage getFrame(){
        return self.imgBuffer;
    }

    public static void clearFrameBuf(){
        Arrays.fill(self.frameBuffer,(byte)255);
    }

    private void setPixelData(){
        DataBuffer buffer = new DataBufferByte(new byte[(this.screenWidth*this.screenHeight)*4],(this.screenWidth*this.screenHeight)*4);
        WritableRaster raster = Raster.createInterleavedRaster(buffer, this.screenWidth, this.screenHeight, this.screenWidth * 4, 4, new int[] { 3, 2, 1, 0 }, null);
        ColorModel cModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),true,true,Transparency.TRANSLUCENT,DataBuffer.TYPE_BYTE);
        this.imgBuffer = new BufferedImage(cModel,raster,cModel.isAlphaPremultiplied(),null);
        this.frameBuffer = ((DataBufferByte)this.imgBuffer.getRaster().getDataBuffer()).getData();
    }

    public static void setPixel(int x,int y,int color){
        if(self.inBounds(y,x)) {
            int i = self.getIndex(x, y);
            if (i != -1){
                if(((color) & 255) == 0)return;
                self.frameBuffer[i] = (byte) (color);
                self.frameBuffer[i + 1] = (byte) (color >> 8);
                self.frameBuffer[i + 2] = (byte) (color >> 16);
                self.frameBuffer[i + 3] = (byte) (color >> 24);
            }
        }
    }

    public static void setPixel(Vec2d pixel, Pixel pixelF){
        if(self.inBounds(pixel.y,pixel.x)) {
            int i = self.getIndex(pixel.x,pixel.y);
            if (i != -1){
                if((pixelF.alpha & 255) == 0)return;
                self.frameBuffer[i] = pixelF.alpha;
                self.frameBuffer[i + 1] = pixelF.blue;
                self.frameBuffer[i + 2] = pixelF.green;
                self.frameBuffer[i + 3] = pixelF.red;
            }
        }
    }

    public static int getPixel(int x,int y){
        if(self.inBounds(y,x)) {
            return self.getFrameBufferColor(self.getIndex(x, y));
        }
        return 0xffffffff;
    }

    boolean inBounds(int row,int col){
        return (col >=0 && col< self.screenWidth && row >= 0 && row < self.screenHeight);
    }

    private int getIndex(int x,int y){
        int i = ((y*this.screenWidth)+x)*4;
        return (i <this.frameBuffer.length-4) ? i : -1;
    }

    int getFrameBufferColor(int index){
        int color = 0x0;
        if(index >= 0 && index < self.frameBuffer.length-3){
            color <<=8;
            color|=self.frameBuffer[index+3] & 0xff; // red
            color <<=8;
            color|=self.frameBuffer[index+2] & 0xff; // green
            color <<=8;
            color|=self.frameBuffer[index+1] & 0xff; // blue
            color <<=8;
            color|=self.frameBuffer[index] & 0xff;// alpha
            return color;
        }
        return 0xffffffff;
    }

}
