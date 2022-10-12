package helper.font.ttf;

import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;

import static helper.methods.CommonMethods.bytesToInt;

public class TTFhheaInfo implements ITTFTableInfo {
    public short ascent,descent,lineGap,advanceWidthMax,
            minLeftSideBearing,minRightSideBearing,xMaxExtent,caretSlopeRise,
            caretSlopeRun,caretOffset,metricDataFormat ,numOfLongHorMetrics ;
    public int version;

    @Override
    public void setValues(byte[] buf){
        version = bytesToInt(buf,0,4,false) / (1 << 16);           // read = 4
        ascent = (short)bytesToInt(buf,4,2,false);                 // read = 6
        descent = (short)bytesToInt(buf,6,2,false);                // read = 8
        lineGap = (short)bytesToInt(buf,8,2,false);                // read = 10
        advanceWidthMax = (short)bytesToInt(buf,10,2,false);       // read = 12
        minLeftSideBearing = (short)bytesToInt(buf,12,2,false);    // read = 14
        minRightSideBearing = (short)bytesToInt(buf,14,2,false);   // read = 16
        xMaxExtent = (short)bytesToInt(buf,16,2,false);            // read = 18
        caretSlopeRise = (short)bytesToInt(buf,18,2,false);        // read = 20
        caretSlopeRun = (short)bytesToInt(buf,20,2,false);         // read = 22
        caretOffset = (short)bytesToInt(buf,22,2,false);           // read = 24
        //skip 4 short                                             // read += 8
        metricDataFormat = (short)bytesToInt(buf,32,2,false);      // read = 34
        numOfLongHorMetrics = (short)bytesToInt(buf,34,2,false);   // read = 36
    }

    @Override
    public Object getValues(){return numOfLongHorMetrics;}

    @Override
    public void dumpValues(){
        IOHandler.printString("---------HHEAINFO----------");
        IOHandler.printString("version %d".formatted(version));
        IOHandler.printString("ascent %d".formatted(ascent));
        IOHandler.printString("descent %d".formatted(descent));
        IOHandler.printString("lineGap %d".formatted(lineGap));
        IOHandler.printString("advanceWidthMax %d".formatted(advanceWidthMax));
        IOHandler.printString("minLeftSideBearing %d".formatted(minLeftSideBearing));
        IOHandler.printString("minRightSideBearing %d".formatted(minRightSideBearing));
        IOHandler.printString("xMaxExtent %s".formatted(xMaxExtent));
        IOHandler.printString("caretSlopeRise %s".formatted(caretSlopeRise));
        IOHandler.printString("caretSlopeRun %d".formatted(caretSlopeRun));
        IOHandler.printString("caretOffset %d".formatted(caretOffset));
        IOHandler.printString("metricDataFormat %d".formatted(metricDataFormat));
        IOHandler.printString("numOfLongHorMetrics %d".formatted(numOfLongHorMetrics));
        IOHandler.printString("---------END----------");
    }

    @Override
    public void checkForValuesBelowZero(){
        IOHandler.printString("HHEAINFO Checking For Values Below Zero\nIf Found Maybe Switch To Larger DataType Because Lack Of Unsigned");
        if(version<0)IOHandler.printString("version %d".formatted(version));
        if(ascent<0)IOHandler.printString("ascent %d".formatted(ascent));
        if(descent<0)IOHandler.printString("descent %d".formatted(descent));
        if(lineGap<0)IOHandler.printString("lineGap %d".formatted(lineGap));
        if(advanceWidthMax<0)IOHandler.printString("advanceWidthMax %d".formatted(advanceWidthMax));
        if(minLeftSideBearing<0)IOHandler.printString("minLeftSideBearing %d".formatted(minLeftSideBearing));
        if(minRightSideBearing<0)IOHandler.printString("minRightSideBearing %d".formatted(minRightSideBearing));
        if(xMaxExtent<0)IOHandler.printString("xMaxExtent %d".formatted(xMaxExtent));
        if(caretSlopeRise<0)IOHandler.printString("caretSlopeRise %d".formatted(caretSlopeRise));
        if(caretSlopeRun<0)IOHandler.printString("caretSlopeRun %d".formatted(caretSlopeRun));
        if(caretOffset<0)IOHandler.printString("caretOffset %d".formatted(caretOffset));
        if(metricDataFormat<0)IOHandler.printString("metricDataFormat %d".formatted(metricDataFormat));
        if(numOfLongHorMetrics<0)IOHandler.printString("numOfLongHorMetrics %d".formatted(numOfLongHorMetrics));
    }
}
