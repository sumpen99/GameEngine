package helper.font.ttf;

import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;
import helper.struct.Glyf;

import static helper.methods.CommonMethods.bytesToInt;

public class TTFglyfInfo implements ITTFTableInfo {
    Glyf[] glyf;
    int multiplier,size;
    short[] locaOffsetsShort;
    int[] locaOffsetsInt;

    public TTFglyfInfo(short indexToLocalFormat,Object locaoffsets){
        multiplier = indexToLocalFormat == 0 ? 2 : 1;
        if(indexToLocalFormat == 0){
            locaOffsetsShort = (short[])locaoffsets;
            size = locaOffsetsShort.length;
        }
        else{
            locaOffsetsInt = (int[])locaoffsets;
            size = locaOffsetsInt.length;
        }
        glyf = new Glyf[size];
    }

    @Override
    public void setValues(byte[] buf){
        int i=0,offset;
        while(i<size){
            offset = multiplier == 2 ? multiplier*locaOffsetsShort[i] : multiplier*locaOffsetsInt[i];
            glyf[i] = new Glyf(
                    (short)bytesToInt(buf,offset,2,false),
                    (short)bytesToInt(buf,offset+2,2,false),
                    (short)bytesToInt(buf,offset+4,2,false),
                    (short)bytesToInt(buf,offset+6,2,false),
                    (short)bytesToInt(buf,offset+8,2,false));
            i++;
        }

    }

    @Override
    public Object getValues(){
        return glyf;
    }

    @Override
    public void dumpValues(){
        IOHandler.printString("---------TTFGlyfInfo----------");
        int size = glyf.length,i=0;
        while(i<size){
            IOHandler.printGlyph(glyf[i++]);
        }
        IOHandler.printString("---------END----------");
    }

    @Override
    public void checkForValuesBelowZero(){
        IOHandler.printString("TTFHMTXINFO Checking For Values Below Zero\nIf Found Maybe Switch To Larger DataType Because Lack Of Unsigned");
    }
}
