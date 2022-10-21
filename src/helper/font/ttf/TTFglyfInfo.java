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
        int i=0,j,k,l,m,n,offset=0,lastIndex;
        while(i<size){
            offset = multiplier == 2 ? multiplier*locaOffsetsShort[i] : multiplier*locaOffsetsInt[i];
            Glyf g = new Glyf(
                    (short)bytesToInt(buf,offset,2,false),
                    (short)bytesToInt(buf,offset+2,2,false),
                    (short)bytesToInt(buf,offset+4,2,false),
                    (short)bytesToInt(buf,offset+6,2,false),
                    (short)bytesToInt(buf,offset+8,2,false));
            offset+=10;
            if(g.numberOfContours >= 0){ // simple glyf
                j=k=l=m=n=0;
                g.endPtsOfContours = new short[g.numberOfContours];
                while(j<g.numberOfContours){
                    g.endPtsOfContours[j++] = (short)bytesToInt(buf,offset,2,false);
                    offset+=2;
                }

                g.instructionLength = (short)bytesToInt(buf,offset,2,false);
                g.instructions = new byte[g.instructionLength];
                offset+=2;
                while(k<g.instructionLength){
                    g.instructions[k++] = (byte)bytesToInt(buf,offset,1,false);
                    offset++;
                }

                lastIndex = g.endPtsOfContours[g.numberOfContours-1];
                g.flags = new byte[lastIndex+1];
                while(l<(lastIndex+1)){
                    g.flags[l] = (byte)bytesToInt(buf,offset,1,false);
                    offset++;
                    if(g.getRepeat(l)) {
                        byte repeatCount = (byte)bytesToInt(buf,offset,1,false);
                        offset++;
                        while(repeatCount-- > 0){
                            l++;
                            g.flags[l] = g.flags[l-1];
                        }
                    }
                    l++;
                }

                //(0) current coordinate is 16 bit signed delta change.
                //(1) current coordinate is 16 bit, has the same value as the previous one.
                //(2) current coordinate is 8 bit, value is negative.
                //(3) current coordinate is 8 bit, value is positive.

                g.xCoordinates = new int[(lastIndex+1)*2];
                int prevCoordinate=0,currentCoordinate=0;
                while(m<(lastIndex+1)){
                    int flagCombined = g.xShort(m) << 1 | g.xShortPos(m);
                    switch(flagCombined){
                        case 0:{
                            currentCoordinate = bytesToInt(buf,offset,2,false);
                            offset+=2;
                            break;
                        }
                        case 1:{
                            currentCoordinate = 0;
                            break;
                        }
                        case 2:{
                            currentCoordinate = bytesToInt(buf,offset,1,false) * -1;
                            offset++;
                            break;
                        }
                        case 3:{
                            currentCoordinate = bytesToInt(buf,offset,1,false);
                            offset++;
                            break;
                        }
                    }
                    g.xCoordinates[m] = (short)(currentCoordinate+prevCoordinate);
                    prevCoordinate = g.xCoordinates[m];
                    m++;
                }

                g.yCoordinates = new int[(lastIndex+1)*2];
                prevCoordinate=0;
                currentCoordinate=0;
                while(n<(lastIndex+1)){
                    int flagCombined = g.yShort(n) << 1 | g.yShortPos(n);
                    switch(flagCombined){
                        case 0:{
                            currentCoordinate = bytesToInt(buf,offset,2,false);
                            offset+=2;
                            break;
                        }
                        case 1:{
                            currentCoordinate = 0;
                            break;
                        }
                        case 2:{
                            currentCoordinate = bytesToInt(buf,offset,1,false) * -1;
                            offset++;
                            break;
                        }
                        case 3:{
                            currentCoordinate = bytesToInt(buf,offset,1,false);
                            offset++;
                            break;
                        }
                    }
                    g.yCoordinates[n] = (short)(currentCoordinate+prevCoordinate);
                    prevCoordinate = g.yCoordinates[n];
                    n++;
                }
            }
            glyf[i] = g;
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
