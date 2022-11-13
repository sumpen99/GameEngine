package helper.widget;
import helper.drawobjects.Line;
import helper.interfaces.IThreading;
import helper.io.IOHandler;
import helper.struct.Vec2d;
import helper.struct.WaveFile;
import helper.text.TextWriter;
import helper.enums.Color;
import helper.struct.DrawValues;
import helper.struct.SamplePair;

import static helper.methods.CommonMethods.intReMapValue;

public class WaveViewBox extends LabelBox implements IThreading {
    boolean drawInfo;
    SamplePair[] samplePairs;
    SamplePair limitsLowHigh;
    int sampleChunkSize,sampleLineColor,SAMPLE_RANGE=10;
    WaveFile waveFile;
    Vec2d wavePos;
    float duration = 0.0f;
    String playMessage;
    public WaveViewBox(DrawValues dww){
        super(dww);
        lineColor = dww.textColor;
        sampleLineColor = Color.WHITESMOKE.getValue();
        setDrawInfoMode(true);
        wavePos = new Vec2d();
    }

    public void setDrawInfoMode(boolean value){
        drawInfo = value;
    }

    @Override
    public void setBindingValue(Object value){
        /*Class c = value.getClass();
        if(rowValues.getClass() == c){
            rowValues = (String[])value;
        }
        else if(c == SamplePair[].class){
            samplePairs = (SamplePair[])value;
        }
        else if(c == SamplePair.class){
            limitsLowHigh = (SamplePair)value;
        }
        else if(c == Integer.class){
            sampleChunkSize = (int)value;
        }*/
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
        setMessage("Press Play");
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
                TextWriter.drawText(playMessage,fontWidth*2,centerPos.y+fontHeight, lineColor);
                TextWriter.drawText("Duration: %f (Under Construction...)".formatted(duration),fontWidth*2,wObj.getPos().y+wObj.getSize().y, lineColor);
                drawSampleDataPairs();
            }
        }
    }

    void setMessage(String str){
        playMessage = str;
    }

    void drawSampleDataPairs(){
        playSamplePairData(1.0f);
        //Line.drawLine(wObj.getPos().x,wObj.getPos().y+wObj.getSize().y/2,wObj.getPos().x+wObj.getSize().x,wObj.getPos().y+wObj.getSize().y/2,sampleLineColor);
    }

    void playSamplePairData(float offSet){
        int samplePairSize = samplePairs.length,i=0,x;
        int left = wObj.getPos().x,top = wObj.getPos().y,bottom = wObj.getPos().y+wObj.getSize().y,right=wObj.getPos().x+wObj.getSize().x;
        while(i<samplePairSize){
            int yMin = samplePairs[i].minValue,yMax = samplePairs[i].maxValue;
            x = left+(int)(offSet*i)+wavePos.x;
            if(x>right)return;
            if(x >= 0){
                yMin = intReMapValue(yMin,limitsLowHigh.minValue,limitsLowHigh.maxValue,top,bottom);
                yMax = intReMapValue(yMax,limitsLowHigh.minValue,limitsLowHigh.maxValue,top,bottom);
                Line.drawLine(x,yMin,x,yMax,sampleLineColor);
            }
            i++;
        }
    }

    @Override
    public void heavyDuty(){
        if(waveFile == null)return;
        setMessage("Press Stop (when implemented)");
        while(wavePos.x>=-samplePairs.length){
            wavePos.x--;
            try{
                Thread.sleep(SAMPLE_RANGE);
                duration+=(float)SAMPLE_RANGE/1000;
            }
            catch(Exception err){
                IOHandler.logToFile(err.getMessage());
            }
            if(duration>= waveFile.durationInSeconds)break;
        }
        setMessage("Press Play");
    }


}
