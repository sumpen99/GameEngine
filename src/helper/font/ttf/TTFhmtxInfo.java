package helper.font.ttf;

import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;
import helper.struct.HMetrics;

import static helper.methods.CommonMethods.bytesToInt;

public class TTFhmtxInfo implements ITTFTableInfo {
    HMetrics[] hMetrics;
    short[] leftSideBearing;

    public TTFhmtxInfo(short numOfLongHorMetrics,short numGlyphs ){
        hMetrics = new HMetrics[numOfLongHorMetrics];
        leftSideBearing = new short[numGlyphs-numOfLongHorMetrics];
    }

    @Override
    public void setValues(byte[] buf){
        int size = hMetrics.length,i=0,offset = 0;
        while(i<size){
            hMetrics[i++] = new HMetrics((short)bytesToInt(buf,offset,2,false),(short)bytesToInt(buf,offset+2,2,false));
            offset+=4;
        }
        size = leftSideBearing.length;i=0;
        while(i<size){
            leftSideBearing[i++] = (short)bytesToInt(buf,offset,2,false);
            offset+=2;
        }

    }

    @Override
    public Object getValues(){return null;}

    @Override
    public void dumpValues(){
        IOHandler.printString("---------TTFHMTXInfo----------");
        int size = hMetrics.length,i=0;
        while(i<size){
            IOHandler.printString("advanceWidth: %d leftSideBearing: %d".formatted(hMetrics[i].advanceWidth,hMetrics[i].leftSideBearing));
            i++;
        }
        size = leftSideBearing.length;i=0;
        while(i<size){
            IOHandler.printString("leftSideBearingValue: %d".formatted(leftSideBearing[i]));
            i++;
        }
        IOHandler.printString("---------END----------");
    }

    @Override
    public void checkForValuesBelowZero(){
        IOHandler.printString("TTFHMTXINFO Checking For Values Below Zero\nIf Found Maybe Switch To Larger DataType Because Lack Of Unsigned");
        int size = hMetrics.length,i=0;
        while(i<size){
            if(hMetrics[i].advanceWidth < 0 || hMetrics[i].leftSideBearing < 0)IOHandler.printString("advanceWidth: %d leftSideBearing: %d".formatted(hMetrics[i].advanceWidth,hMetrics[i].leftSideBearing));
            i++;
        }
        size = leftSideBearing.length;i=0;
        while(i<size){
            if(leftSideBearing[i] < 0)IOHandler.printString("leftSideBearingValue: %d".formatted(leftSideBearing[i]));
            i++;
        }
    }
}
