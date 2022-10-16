package helper.font.ttf;

import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;
import helper.list.SMHashMap;
import helper.struct.CMap;
import helper.struct.EncodingRecord;
import helper.struct.FormatFour;
import helper.struct.Glyf;

import static helper.enums.EntrieType.ENTRIE_TTF_GLYPHINDEX;
import static helper.methods.CommonMethods.bytesToInt;

public class TTFcmapInfo implements ITTFTableInfo {
    CMap cMap;
    FormatFour format4;
    short version,numTables,format;

    void readFormatFour(byte[] buf,int offset){
        format4 = new FormatFour();
        format4.length = (short)bytesToInt(buf,offset,2,false);
        format4.language = (short)bytesToInt(buf,offset+2,2,false);
        format4.segCountX2 = (short)bytesToInt(buf,offset+4,2,false);
        format4.searchRange = (short)bytesToInt(buf,offset+6,2,false);
        format4.entrySelector = (short)bytesToInt(buf,offset+8,2,false);
        format4.rangeShift = (short)bytesToInt(buf,offset+10,2,false);
        offset+=12;

        int segCount = format4.segCountX2 >> 1,i=0,code,idRangeOffsetStart = -1,cnt = 0;

        format4.endCode = new short[segCount];
        format4.startCode = new short[segCount];
        format4.idDelta = new short[segCount];
        format4.idRangeOffset = new short[segCount];
        format4.segCount = segCount;
        while(i<segCount){
            format4.endCode[i] = (short) bytesToInt(buf, offset, 2, false);
            offset+=2;
            i++;
        }
        offset+=2;//Reserved Pad
        i=0;
        while(i<segCount){
            format4.startCode[i] = (short) bytesToInt(buf, offset, 2, false);
            offset+=2;
            i++;
        }
        i=0;
        while(i<segCount){
            format4.idDelta[i] = (short) bytesToInt(buf, offset, 2, false);
            offset+=2;
            i++;
        }
        i=0;
        idRangeOffsetStart = offset;
        while(i<segCount){
            format4.idRangeOffset[i] = (short) bytesToInt(buf, offset, 2, false);
            offset+=2;
            i++;
        }
        readSegments(buf,segCount,idRangeOffsetStart);
    }

    void readSegments(byte[] buf,int segCount,int idRangeOffsetStart){
        int i = 0;
        while(i<segCount-1){
            int glyphIndex = 0;
            short endCode = format4.endCode[i];
            short startCode = format4.startCode[i];
            short idDelta = format4.idDelta[i];
            short idRangeOffset = format4.idRangeOffset[i];
            for(short c = startCode;c<endCode;c++){
                if(idRangeOffset != 0){
                    int startCodeOffset = (c-startCode) * 2;
                    int currentRangeOffset = i*2;

                    int glyphIndexOffset =  idRangeOffsetStart +
                                            currentRangeOffset +
                                            idRangeOffset +
                                            startCodeOffset;

                    glyphIndex = bytesToInt(buf,glyphIndexOffset,2,false);
                    if(glyphIndex != 0){
                        glyphIndex = (glyphIndex + idDelta) & 0xffff;
                    }
                    else{
                        glyphIndex = (c + idDelta) & 0xffff;
                    }
                    IOHandler.printString("index (%c): %d value glyphindex: %d ".formatted((int)c,c,glyphIndex));
                    format4.glyphIndexMap.addNewItem("%d".formatted((int)c),glyphIndex,ENTRIE_TTF_GLYPHINDEX);
                }
            }
            i++;
        }
    }

    @Override
    public void setValues(byte[] buf){
        int i=0,offset;
        cMap = new CMap();
        version = (short)bytesToInt(buf,0,2,false);
        numTables = (short)bytesToInt(buf,2,2,false);
        cMap.encodingRecords = new EncodingRecord[numTables];
        offset = 4;
        while(i<numTables){
            cMap.encodingRecords[i] = new EncodingRecord((short)bytesToInt(buf,offset,2,false),(short)bytesToInt(buf,offset+2,2,false),bytesToInt(buf,offset+4,4,false));
            offset+=8;
            i++;
        }
        format = (short)bytesToInt(buf,offset,2,false);
        assert format==4 : "Unsupported Format: %s Required 4".formatted(format);
        readFormatFour(buf,offset+2);

    }

    @Override
    public Object getValues(){
        return format4.glyphIndexMap;
    }

    @Override
    public void dumpValues(){
        IOHandler.printString("---------TTFCMAPInfo----------");
        IOHandler.printString("Version: %d numTables: %d format: %d".formatted(version,numTables,format));
        int i = 0;
        while(i<numTables){
            IOHandler.printEncodingRecord(cMap.encodingRecords[i]);
            i++;
        }
        IOHandler.printFormatFour(format4);
        IOHandler.printString("---------END----------");
    }

    @Override
    public void checkForValuesBelowZero(){
        IOHandler.printString("TTFHMTXINFO Checking For Values Below Zero\nIf Found Maybe Switch To Larger DataType Because Lack Of Unsigned");
    }
}
