package helper.struct;

import helper.enums.TTFBits;

import static helper.methods.CommonMethods.bytesToInt;
import static helper.methods.CommonMethods.bytesToLong;

public class TTFHead {
    public short majorVersion,minorVersion,flags,unitsPerEM,
            macStyle,lowRecPPEM,indexToLocalFormat,glyphDataFormat,
            fontDirectionHint;
    //fWord xMin,yMin,xMax,yMax getFWord = getInt16
    //date created,modified getFWord = const getDate = () => {
    //    const macTime = getUint32() * 0x100000000 + getUint32()
    //    const utcTime = macTime * 1000 + Date.UTC(1904, 1, 1)
    //    return new Date(utcTime)
    //  }
    public short xMin,yMin,xMax,yMax;
    public long created,modified;
    public int fontRevision; //getFixed = () => getInt32() / (1 << 16)
    public int checkSumAdjustment,magicNumber;

    public void convertToSize(byte[] buf){
        majorVersion = (short)bytesToInt(buf,0,2,false);        // read = 2
        minorVersion = (short)bytesToInt(buf,2,2,false);        // read = 4
        fontRevision = bytesToInt(buf,4,4,false);               // read = 8
        checkSumAdjustment = bytesToInt(buf,8,4,false);         // read = 12
        magicNumber = bytesToInt(buf,12,4,false);               // read = 16
        flags = (short)bytesToInt(buf,16,2,false);              // read = 18
        unitsPerEM = (short)bytesToInt(buf,18,2,false);         // read = 20
        created = bytesToLong(buf,20,8,false);                  // read = 28
        modified = bytesToLong(buf,28,8,false);                 // read = 36
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

}
