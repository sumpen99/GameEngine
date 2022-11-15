package helper.widget;
import engine.GameEngine;
import helper.drawobjects.Line;
import helper.interfaces.IThreading;
import helper.io.IOHandler;
import helper.struct.Vec2d;
import helper.struct.WaveFile;
import helper.text.TextWriter;
import helper.enums.Color;
import helper.struct.DrawValues;
import helper.struct.SamplePair;

import static helper.enums.WidgetState.SM_TOUCH_PROCESSED;
import static helper.methods.CommonMethods.intReMapValue;

public class WaveViewBox extends LabelBox implements IThreading {
    boolean drawInfo,isPlaying;
    SamplePair[] samplePairs;
    SamplePair limitsLowHigh;
    int sampleChunkSize,sampleLineColor,SAMPLE_RANGE=1;
    WaveFile waveFile;
    Vec2d wavePos;
    float duration = 0.0f,WAVE_OFFSET = 1.0f;
    public WaveViewBox(DrawValues dww){
        super(dww);
        lineColor = dww.textColor;
        sampleLineColor = Color.WHITESMOKE.getValue();
        setDrawInfoMode(true);
        wavePos = new Vec2d();
        startPos = new Vec2d();
        mPos = new Vec2d();
    }

    @Override
    public boolean onMouseLeftDown(int x,int y){
        if(touchEventNotProcessed()){
            setWidgetBit(SM_TOUCH_PROCESSED.getValue());
            startPos.x = x;
            startPos.y = y;
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseReleaseTouch(int x,int y){
        if(getWidgetBitSet(SM_TOUCH_PROCESSED.getIndex())){
            clearWidgetBits();
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseLeftMove(int x,int y){
        if(getWidgetBitSet(SM_TOUCH_PROCESSED.getIndex())){
            mPos.x = x-startPos.x;
            mPos.y = y-startPos.y;
            wavePos.x+=mPos.x;
            startPos.x = x;
            startPos.y = y;
            return true;
        }
        return false;
    }

    public void setDrawInfoMode(boolean value){
        drawInfo = value;
    }

    @Override
    public void setBindingValue(Object value){
        if(WaveFile.class == value.getClass()){
            waveFile = (WaveFile)value;
            setWaveFileInfo();
        }
        else if(Boolean.class == value.getClass()){
            drawInfo = (boolean)value;
        }
    }

    void setWaveFileInfo(){
        rowValues = waveFile.getFileInfo();
        sampleChunkSize = waveFile.getSampleChunkSize();
        limitsLowHigh = waveFile.getLimitsLowHigh();
        samplePairs = waveFile.getSamplePairs();
        wavePos.x = wObj.getSize().x;
    }

    void reWind(){
        pressPlayButton();
        //wavePos.x = wObj.getSize().x;
        duration = 0.0f;
        childWidget.setBindingValue("Play"); // Play Button
    }

    public boolean getDurationStatus(){
        return duration == 0.0f;
    }

    public void rewindPosition(){
        wavePos.x = wObj.getSize().x;
    }

    public void pressPlayButton(){
        isPlaying^=true;
    }

    public boolean isAudioPlaying(){
        return isPlaying;
    }

    @Override
    public void draw() {
        wObj.draw();
        if(!drawInfo){
            for (int i = 0; i < rowValues.length; i++) {
                if(rowValues[i] != null){
                    getCenterPos(i);
                    TextWriter.drawText("%s".formatted(rowValues[i]),fontWidth*2,centerPos.y+fontHeight, lineColor);
                }
            }
        }
        else{
            if(samplePairs != null){
                getCenterPos(0);
                TextWriter.drawText("Duration: %f (Under Construction...)".formatted(duration),fontWidth*2,wObj.getPos().y+wObj.getSize().y, lineColor);
                drawSampleDataPairs();
            }
        }
    }

    void drawSampleDataPairs(){
        playSamplePairData();
        //Line.drawLine(wObj.getPos().x,wObj.getPos().y+wObj.getSize().y/2,wObj.getPos().x+wObj.getSize().x,wObj.getPos().y+wObj.getSize().y/2,sampleLineColor);
    }

    void playSamplePairData(){
        int samplePairSize = samplePairs.length,i=0,x;
        int left = wObj.getPos().x,top = wObj.getPos().y,bottom = wObj.getPos().y+wObj.getSize().y,right=wObj.getPos().x+wObj.getSize().x;
        //int lastYMax = samplePairs[0].maxValue;
        //int lastX = left+wavePos.x;
        while(i<samplePairSize){
            int yMin = samplePairs[i].minValue,yMax = samplePairs[i].maxValue;
            x = left+(int)(WAVE_OFFSET*i)+wavePos.x;
            if(x>right){
                //yMax = intReMapValue(.5f,0.0f,1.0f,top,bottom);
                //Line.drawLine(left,yMax,right,yMax,sampleLineColor);
                return;
            }
            if(x >= 0){
                yMin = intReMapValue(yMin,limitsLowHigh.minValue,limitsLowHigh.maxValue,top,bottom);
                yMax = intReMapValue(yMax,limitsLowHigh.minValue,limitsLowHigh.maxValue,top,bottom);
                Line.drawLine(x,yMin,x,yMax,sampleLineColor);
                //Line.drawLine(lastX,lastYMax,x,yMin,sampleLineColor);
                //lastX = x;
                //lastYMax = yMax;
            }
            i++;
        }
    }

    @Override
    public void heavyDuty(){
        if(waveFile == null)return;
        while(duration<=waveFile.durationInSeconds && isPlaying){
            wavePos.x--;
            try{
                Thread.sleep(SAMPLE_RANGE);
                duration+=(float)SAMPLE_RANGE/1000;
            }
            catch(Exception err){
                IOHandler.logToFile(err.getMessage());
            }
            //if(duration>= waveFile.durationInSeconds)break;
        }
        if(isPlaying){
            reWind();
        }
    }


}
