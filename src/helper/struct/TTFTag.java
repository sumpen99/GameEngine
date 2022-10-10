package helper.struct;

public class TTFTag {
    public int checkSum,offset,length;
    public String tag;
    public TTFTag(String t,int c,int o,int l){
        tag = t;
        checkSum = c;
        offset = o;
        length = l;
    }
}
