package helper.font.ttf;

import helper.enums.TTFTable;
import helper.interfaces.ITTFTableInfo;

public class TTFTableTag {
    public int checkSum,offset,length;
    public TTFTable tag;
    public ITTFTableInfo tableValues;
    public TTFTableTag(TTFTable t, int c, int o, int l){
        tag = t;
        checkSum = c;
        offset = o;
        length = l;
    }

    public void setTableValues(ITTFTableInfo obj){
        tableValues = obj;
    }

    public ITTFTableInfo getTableValue(){
        return tableValues;
    }
}
