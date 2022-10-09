package helper.widget;
import helper.drawobjects.Line;
import helper.drawobjects.TextWriter;
import helper.enums.Color;
import helper.io.IOHandler;
import helper.struct.DrawValues;
import helper.struct.SamplePair;

import static helper.methods.CommonMethods.intReMapValue;

public class WaveViewBox extends LabelBox{
    boolean drawInfo;
    SamplePair[] samplePairs;
    SamplePair limitsLowHigh;
    int sampleChunkSize,sampleLineColor;
    public WaveViewBox(DrawValues dww){
        super(dww);
        lineColor = dww.textColor;
        sampleLineColor = Color.WHITESMOKE.getValue();
        setDrawInfoMode(true);
    }

    public void setDrawInfoMode(boolean value){
        drawInfo = value;
    }

    @Override
    public void setBindingValue(Object value){
        Class c = value.getClass();
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
        }
        else if(Boolean.class == c){
            drawInfo = (boolean)value;
        }
    }

    @Override
    public void draw() {
        wObj.draw();
        if(drawInfo){
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
                TextWriter.drawText("Sample ChunkSize: %s".formatted(sampleChunkSize),fontWidth*2,centerPos.y+fontHeight, lineColor);
                drawSampleDataPairs();
            }
        }
    }

    void drawSampleDataPairs(){
        int samplePairSize = samplePairs.length,i=0,x;
        float offSet = (float)wObj.getSize().x/samplePairSize;
        int left = wObj.getPos().x,top = wObj.getPos().y,bottom = wObj.getPos().y+wObj.getSize().y;
        while(i<samplePairSize){
            int yMin = samplePairs[i].minValue,yMax = samplePairs[i].maxValue;
            x = left+(int)(offSet*i);
            yMin = intReMapValue(yMin,limitsLowHigh.minValue,limitsLowHigh.maxValue,top,bottom);
            yMax = intReMapValue(yMax,limitsLowHigh.minValue,limitsLowHigh.maxValue,top,bottom);
            Line.drawLine(x,yMin,x,yMax,sampleLineColor);
            i++;
        }
    }
}
