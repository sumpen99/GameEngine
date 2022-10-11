package helper.font.ttf;
import helper.enums.TTFTable;
import helper.interfaces.ITTFTable;
import helper.interfaces.ITTFTableInfo;
import helper.struct.SMDateTime;
import static helper.methods.CommonMethods.bytesToInt;


public class TTFTableHead extends TTFTableBase{
    public TTFTableHead(){
        super();
        self = new TTFHeadInfo();
    }

    @Override
    public int getTableOffset(TTFFile header){
        return header.getTableOffset(TTFTable.HEAD);
    }

    @Override
    public int getTableLength(TTFFile header){
        return header.getTableLength(TTFTable.HEAD);
    }

    @Override
    public void setTableValue(TTFFile header){
        header.setTableValue(TTFTable.HEAD,self);
    }

    @Override
    public void convertToSize(byte[] buf){
        self.majorVersion = (short)bytesToInt(buf,0,2,false);        // read = 2
        self.minorVersion = (short)bytesToInt(buf,2,2,false);        // read = 4
        self.fontRevision = bytesToInt(buf,4,4,false) / (1 << 16);   // read = 8
        self.checkSumAdjustment = bytesToInt(buf,8,4,false);         // read = 12
        self.magicNumber = bytesToInt(buf,12,4,false);               // read = 16
        self.flags = (short)bytesToInt(buf,16,2,false);              // read = 18
        self.unitsPerEM = (short)bytesToInt(buf,18,2,false);         // read = 20
        self.created = parseDate(buf,20);                            // read = 28
        self.modified = parseDate(buf,28);                           // read = 36
        self.xMin = (short)bytesToInt(buf,36,2,false);               // read = 38
        self.yMin = (short)bytesToInt(buf,38,2,false);               // read = 40
        self.xMax = (short)bytesToInt(buf,40,2,false);               // read = 42
        self.yMax = (short)bytesToInt(buf,42,2,false);               // read = 44
        self.macStyle = (short)bytesToInt(buf,44,2,false);           // read = 46
        self.lowRecPPEM = (short)bytesToInt(buf,46,2,false);         // read = 48
        self.fontDirectionHint = (short)bytesToInt(buf,48,2,false);  // read = 50
        self.indexToLocalFormat = (short)bytesToInt(buf,50,2,false); // read = 52
        self.glyphDataFormat = (short)bytesToInt(buf,52,2,false);    // read = 54

    }

    String parseDate(byte[] buf,int offset){
        long macTime = bytesToInt(buf,offset,4,false) * 0x100000000L + bytesToInt(buf,offset+4,4,false);
        long utcTime = macTime * 1000 + SMDateTime.getMilliSeconds();
        return SMDateTime.getDateFromMilliseconds(utcTime);

    }

}
