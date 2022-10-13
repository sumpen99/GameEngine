package helper.font.ttf;

import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;
import helper.struct.CMap;
import helper.struct.EncodingRecord;
import helper.struct.Glyf;

import static helper.methods.CommonMethods.bytesToInt;

public class TTFcmapInfo implements ITTFTableInfo {
    CMap cMap;
    short version,numTables,format;

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
        offset+=2;
    }

    @Override
    public Object getValues(){return null;}

    @Override
    public void dumpValues(){
        IOHandler.printString("---------TTFCMAPInfo----------");
        IOHandler.printString("Version: %d numTables: %d format: %d".formatted(version,numTables,format));
        int i = 0;
        while(i<numTables){
            IOHandler.printEncodingRecord(cMap.encodingRecords[i]);
            i++;
        }
        IOHandler.printString("---------END----------");
    }

    @Override
    public void checkForValuesBelowZero(){
        IOHandler.printString("TTFHMTXINFO Checking For Values Below Zero\nIf Found Maybe Switch To Larger DataType Because Lack Of Unsigned");
    }
}
