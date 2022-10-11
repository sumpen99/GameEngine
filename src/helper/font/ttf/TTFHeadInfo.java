package helper.font.ttf;

import helper.interfaces.ITTFTableInfo;
import helper.io.IOHandler;
import helper.struct.SMDateTime;

import static helper.methods.CommonMethods.bytesToInt;

public class TTFHeadInfo implements ITTFTableInfo {
    public short majorVersion,minorVersion,flags,unitsPerEM,
            macStyle,lowRecPPEM,indexToLocalFormat,glyphDataFormat,
            fontDirectionHint,xMin,yMin,xMax,yMax;
    public String created,modified;
    public int fontRevision,checkSumAdjustment,magicNumber;

    @Override
    public void setValues(byte[] buf){
        majorVersion = (short)bytesToInt(buf,0,2,false);        // read = 2
        minorVersion = (short)bytesToInt(buf,2,2,false);        // read = 4
        fontRevision = bytesToInt(buf,4,4,false) / (1 << 16);   // read = 8
        checkSumAdjustment = bytesToInt(buf,8,4,false);         // read = 12
        magicNumber = bytesToInt(buf,12,4,false);               // read = 16
        flags = (short)bytesToInt(buf,16,2,false);              // read = 18
        unitsPerEM = (short)bytesToInt(buf,18,2,false);         // read = 20
        created = parseDate(buf,20);                            // read = 28
        modified = parseDate(buf,28);                           // read = 36
        xMin = (short)bytesToInt(buf,36,2,false);               // read = 38
        yMin = (short)bytesToInt(buf,38,2,false);               // read = 40
        xMax = (short)bytesToInt(buf,40,2,false);               // read = 42
        yMax = (short)bytesToInt(buf,42,2,false);               // read = 44
        macStyle = (short)bytesToInt(buf,44,2,false);           // read = 46
        lowRecPPEM = (short)bytesToInt(buf,46,2,false);         // read = 48
        fontDirectionHint = (short)bytesToInt(buf,48,2,false);  // read = 50
        indexToLocalFormat = (short)bytesToInt(buf,50,2,false); // read = 52
        glyphDataFormat = (short)bytesToInt(buf,52,2,false);    // read = 54
    }

    String parseDate(byte[] buf,int offset){
        long macTime = bytesToInt(buf,offset,4,false) * 0x100000000L + bytesToInt(buf,offset+4,4,false);
        long utcTime = macTime * 1000 + SMDateTime.getMilliSeconds();
        return SMDateTime.getDateFromMilliseconds(utcTime);

    }

   @Override
    public void dumpValues(){
        IOHandler.printString("majorVersion %d".formatted(majorVersion));
        IOHandler.printString("minorVersion %d".formatted(minorVersion));
        IOHandler.printString("fontRevision %d".formatted(fontRevision));
        IOHandler.printString("checkSumAdjustment %d".formatted(checkSumAdjustment));
        IOHandler.printString("magicNumber %d".formatted(magicNumber));
        IOHandler.printString("flags %d".formatted(flags));
        IOHandler.printString("unitsPerEM %d".formatted(unitsPerEM));
        IOHandler.printString("created %s".formatted(created));
        IOHandler.printString("modified %s".formatted(modified));
        IOHandler.printString("xMin %d".formatted(xMin));
        IOHandler.printString("yMin %d".formatted(yMin));
        IOHandler.printString("xMax %d".formatted(xMax));
        IOHandler.printString("yMax %d".formatted(yMax));
        IOHandler.printString("macStyle %d".formatted(macStyle));
        IOHandler.printString("lowRecPPEM %d".formatted(lowRecPPEM));
        IOHandler.printString("fontDirectionHint %d".formatted(fontDirectionHint));
        IOHandler.printString("indexToLocalFormat %d".formatted(indexToLocalFormat));
        IOHandler.printString("glyphDataFormat %d".formatted(glyphDataFormat));
    }
}
