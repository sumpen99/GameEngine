package helper.font.ttf;
import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;


import static helper.methods.CommonMethods.bytesToInt;

public class TTFlocaInfo implements ITTFTableInfo {
    short[] locaShort;
    int[] locaInt;
    boolean useLocaShort;
    short sizeToRead;

    public TTFlocaInfo(short indexToLocalFormat,short numGlyphs){
        useLocaShort = indexToLocalFormat == 0;
        sizeToRead = numGlyphs;
        if(useLocaShort)locaShort = new short[numGlyphs];
        else locaInt = new int[numGlyphs];
    }

    @Override
    public void setValues(byte[] buf){
        int i=0,offset = 0;
        while(i<sizeToRead){
            if(useLocaShort){
                locaShort[i] = (short)bytesToInt(buf,offset,2,false);
                offset+=2;
            }
            else{
                locaInt[i] = bytesToInt(buf,offset,4,false);
                offset+=4;
            }
            i++;
        }
    }

    @Override
    public Object getValues(){return null;}

    @Override
    public void dumpValues(){
        IOHandler.printString("---------TTFHMTXInfo----------");
        int i=0;
        while(i<sizeToRead){
            if(useLocaShort){IOHandler.printString("ShortValue: %d".formatted(locaShort[i]));}
            else{IOHandler.printString("IntValue: %d".formatted(locaInt[i]));}
            i++;
        }
        IOHandler.printString("---------END----------");
    }

    @Override
    public void checkForValuesBelowZero(){
        IOHandler.printString("TTFHMTXINFO Checking For Values Below Zero\nIf Found Maybe Switch To Larger DataType Because Lack Of Unsigned");
        int i=0;
        while(i<sizeToRead){
            if(useLocaShort){
                if(locaShort[i]<0)IOHandler.printString("ShortValue: %d".formatted(locaShort[i]));
            }
            if(useLocaShort){
                if(locaInt[i]<0)IOHandler.printString("IntValue: %d".formatted(locaInt[i]));
            }
            i++;
        }

    }
}
