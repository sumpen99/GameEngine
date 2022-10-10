package helper.struct;

import helper.enums.TTFTableTag;

public class TTFTag {
    public int checkSum,offset,length;
    public TTFTableTag tag;
    public TTFTag(TTFTableTag t,int c,int o,int l){
        tag = t;
        checkSum = c;
        offset = o;
        length = l;
    }
}
