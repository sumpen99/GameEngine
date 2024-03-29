package program;
import engine.GameEngine;
import helper.canvas.CanvasHandler;
import helper.enums.BorderMethod;
import helper.enums.Color;
import helper.enums.SampleMethod;
import helper.input.KeyboardHandler;
import helper.input.MouseHandler;
import helper.io.IOHandler;
import helper.struct.*;

import static helper.enums.BorderMethod.*;
import static helper.enums.SampleMethod.*;


public class ImageSampling extends GameEngine {
    Texture tex;
    Vec2d vPixel = new Vec2d();
    BorderMethod border = BLANK;
    SampleMethod sample = POINT;
    Pixel pixelF;
    FVec2d vWorldSample;
    public ImageSampling(int width,int height){
        super(width,height);
    }

    @Override
    public boolean setUpProgram(){
        tex = new Texture();
        tex.loadFromFile("zonfri.png");
        border = BLANK;
        MouseHandler.setScaleFactor(tex.getSize());
        return this.parseGuiFile("./resources/files/gui/imagesampling.fs");
    }

    public void setBorderType(){
        switch(KeyboardHandler.getLastKey()){
            case '1':{border = BLANK;break;}
            case '2':{border = CLAMP;break;}
            case '3':{border = PERIODIC;break;}
            case '4':{border = REFLECTED_PERIODIC;break;}
        }
    }

    public void setSampleType(){
        switch(KeyboardHandler.getLastKey()){
            case '5':{sample = POINT;break;}
            case '6':{sample = BICUBIC;break;}
            case '7':{sample = BILINEAR;break;}
        }
    }

    @Override
    public void onUserUpdate(float fElapsedTime){
        setBorderType();
        setSampleType();
        for(vPixel.y = 0;vPixel.y<screenHeight;vPixel.y++){
            for(vPixel.x = 0;vPixel.x < screenWidth;vPixel.x++){
                vWorldSample = MouseHandler.screenToWorld(vPixel);
                //pixelF = new Pixel(vWorldSample.x,vWorldSample.y,0.0f,1.0f);
                pixelF = tex.sample(vWorldSample,sample,border);
                CanvasHandler.setPixel(vPixel,pixelF);
            }
        }
    }
}