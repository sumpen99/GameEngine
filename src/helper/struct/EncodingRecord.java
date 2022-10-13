package helper.struct;

public class EncodingRecord {
    public short platformID,encodingID;
    public int offSet;
    public boolean isWindowsPlatform,isUniCodePlatform;
    public EncodingRecord(short platformid,short encodingid,int offset){
        platformID = platformid;
        encodingID = encodingid;
        offSet = offset;
        setPlatform();
    }

    void setPlatform(){
        isWindowsPlatform = platformID == 3 && (encodingID == 0 || encodingID == 1 || encodingID == 10);
        isUniCodePlatform = platformID == 0 && (encodingID >= 0 && encodingID <= 4);
    }
}
